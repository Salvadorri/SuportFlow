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
import com.suportflow.backend.service.user.UserManagementService;
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
    private final UserManagementService userService;
    private final ClienteRepository clienteRepository;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    public AuthenticationService(
            AuthenticationHelper authenticationHelper,
            UserDetailsServiceImpl userDetailsService,
            ClienteDetailsService clienteDetailsService,
            UserManagementService userService,
            ClienteRepository clienteRepository,
            JwtUtil jwtUtil,
            RefreshTokenService refreshTokenService) {
        this.authenticationHelper = authenticationHelper;
        this.userDetailsService = userDetailsService;
        this.clienteDetailsService = clienteDetailsService;
        this.userService = userService;
        this.clienteRepository = clienteRepository;
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
    }

    @Transactional
    public AuthenticationResponse authenticateAndGenerateToken(AuthenticationRequest authenticationRequest) {
        String email = authenticationRequest.getEmail();
        String password = authenticationRequest.getPassword();
        System.out.println("Tentando autenticar: " + email);
        System.out.println("Usuário existe: " + userRepository.existsByEmail(email));
        System.out.println("Cliente existe: " + clienteRepository.existsByEmail(email));
        // Variables to store result
        UserDetails userDetails = null;
        RefreshToken refreshToken = null;

        // First, try to authenticate as a regular user

        // Try to authenticate the user directly with our helper
        userDetails = authenticationHelper.authenticateUser(email, password);

        // If authentication succeeded, get the user for the refresh token
        User user = userService.findByEmail(email);
        refreshToken = refreshTokenService.createRefreshToken(user.getId());

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
                        userDetails = userDetailsService.loadUserByUsername(refreshToken.getUser().getEmail());
                    } else if (refreshToken.getCliente() != null) {
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