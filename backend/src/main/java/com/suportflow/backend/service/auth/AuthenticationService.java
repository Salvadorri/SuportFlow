// src/main/java/com/suportflow/backend/service/auth/AuthenticationService.java
package com.suportflow.backend.service.auth;

import com.suportflow.backend.dto.AuthenticationRequest;
import com.suportflow.backend.dto.AuthenticationResponse;
import com.suportflow.backend.dto.RefreshTokenDTO;
import com.suportflow.backend.exception.TokenRefreshException;
import com.suportflow.backend.model.Cliente;
import com.suportflow.backend.model.RefreshToken;
import com.suportflow.backend.model.User;
import com.suportflow.backend.repository.ClienteRepository;
import com.suportflow.backend.repository.UserRepository;
import com.suportflow.backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthenticationService {

    private final AuthenticationHelper authenticationHelper;
    private final UserDetailsServiceImpl userDetailsService;
    private final ClienteDetailsService clienteDetailsService;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;  // Added for direct access
    private final ClienteRepository clienteRepository;

    @Autowired
    public AuthenticationService(
            AuthenticationHelper authenticationHelper,
            UserDetailsServiceImpl userDetailsService,
            ClienteDetailsService clienteDetailsService,
            JwtUtil jwtUtil,
            RefreshTokenService refreshTokenService,
            UserRepository userRepository,
            ClienteRepository clienteRepository) {
        this.authenticationHelper = authenticationHelper;
        this.userDetailsService = userDetailsService;
        this.clienteDetailsService = clienteDetailsService;
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
        this.userRepository = userRepository;
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public AuthenticationResponse authenticateAndGenerateToken(AuthenticationRequest authenticationRequest) {
        String email = authenticationRequest.getEmail();
        String password = authenticationRequest.getPassword();

        UserDetails userDetails = null;
        RefreshToken refreshToken = null;
        Long entityId = null; // To store either user or client ID

        try {
            // First, try to authenticate as a regular user
            userDetails = authenticationHelper.authenticateUser(email, password);
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
            entityId = user.getId(); // Get User ID
            refreshToken = refreshTokenService.createRefreshToken(entityId);

        } catch (UsernameNotFoundException | BadCredentialsException userException) {
            try {
                userDetails = authenticationHelper.authenticateCliente(email, password);
                Cliente cliente = clienteRepository.findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException("Cliente not found: "+ email));
                entityId = cliente.getId();
                refreshToken = refreshTokenService.createRefreshToken(entityId);


            } catch (UsernameNotFoundException | BadCredentialsException clientException) {
                throw new BadCredentialsException("Invalid credentials for both user and client.");
            }
        }

        // Generate JWT token
        String jwt = jwtUtil.generateToken(userDetails);
        return new AuthenticationResponse(jwt, refreshToken.getToken());
    }
    @Transactional
    public AuthenticationResponse refreshToken(RefreshTokenDTO refreshTokenDTO) {
        String requestRefreshToken = refreshTokenDTO.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(refreshToken -> {
                    UserDetails userDetails = null;

                    if (refreshToken.getUser() != null) {
                        System.out.println("Refreshing token for user: " + refreshToken.getUser().getEmail());
                        userDetails = userDetailsService.loadUserByUsername(refreshToken.getUser().getEmail());
                    } else if (refreshToken.getCliente() != null) {
                        System.out.println("Refreshing token for cliente: " + refreshToken.getCliente().getEmail());
                        userDetails = clienteDetailsService.loadUserByUsername(refreshToken.getCliente().getEmail());
                    } else {
                        throw new TokenRefreshException(requestRefreshToken,
                                "Refresh token not associated with any user or client.");
                    }

                    String token = jwtUtil.generateToken(userDetails);
                    return new AuthenticationResponse(token, requestRefreshToken);
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Invalid or missing refresh token."));
    }
}