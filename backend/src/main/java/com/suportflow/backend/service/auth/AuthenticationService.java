// src/main/java/com/suportflow/backend/service/auth/AuthenticationService.java
package com.suportflow.backend.service.auth;

import com.suportflow.backend.dto.AuthenticationRequest;
import com.suportflow.backend.dto.AuthenticationResponse;
import com.suportflow.backend.dto.RefreshTokenDTO;
import com.suportflow.backend.exception.TokenRefreshException;
import com.suportflow.backend.exception.UserNotFoundException; // Import custom exception
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
    private final UserRepository userRepository;
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
                    .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado: " + email)); //Should not happen.
            entityId = user.getId(); // Get User ID
            refreshToken = refreshTokenService.createRefreshToken(entityId);

        } catch (UserNotFoundException | BadCredentialsException userException) {
            try {
                userDetails = authenticationHelper.authenticateCliente(email, password);
                Cliente cliente = clienteRepository.findByEmail(email)
                        .orElseThrow(() -> new UserNotFoundException("Cliente não encontrado: " + email)); //Should not happen.
                entityId = cliente.getId();
                refreshToken = refreshTokenService.createRefreshToken(entityId);


            } catch (UserNotFoundException | BadCredentialsException clientException) {
                // Throw a single exception to simplify handling in the controller.
                throw new BadCredentialsException("Credenciais inválidas.");
            }
        }

        // Generate JWT token,  passing the entityId
        String jwt = jwtUtil.generateToken(userDetails, entityId);
        return new AuthenticationResponse(jwt, refreshToken.getToken());
    }

    @Transactional
    public AuthenticationResponse refreshToken(RefreshTokenDTO refreshTokenDTO) {
        String requestRefreshToken = refreshTokenDTO.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(refreshToken -> {
                    UserDetails userDetails = null;
                    Long entityId = null;

                    if (refreshToken.getUser() != null) {
                        System.out.println("Refreshing token for user: " + refreshToken.getUser().getEmail());
                        userDetails = userDetailsService.loadUserByUsername(refreshToken.getUser().getEmail());
                        entityId = refreshToken.getUser().getId(); // Get User ID
                    } else if (refreshToken.getCliente() != null) {
                        System.out.println("Refreshing token for cliente: " + refreshToken.getCliente().getEmail());
                        userDetails = clienteDetailsService.loadUserByUsername(refreshToken.getCliente().getEmail());
                        entityId = refreshToken.getCliente().getId(); // Get Cliente ID
                    } else {
                        throw new TokenRefreshException(requestRefreshToken,
                                "Refresh token não está associado a nenhum usuário ou cliente.");
                    }

                    String token = jwtUtil.generateToken(userDetails, entityId); // Pass entityId
                    return new AuthenticationResponse(token, requestRefreshToken);
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Token de atualização inválido ou ausente."));
    }
}