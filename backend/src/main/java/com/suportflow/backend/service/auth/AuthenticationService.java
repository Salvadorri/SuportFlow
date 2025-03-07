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
import com.suportflow.backend.security.JwtUtil;
import com.suportflow.backend.service.user.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService; // Para usuários
    @Autowired
    private ClienteDetailsService clienteDetailsService; // Para clientes
    @Autowired
    private UserManagementService userService;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private RefreshTokenService refreshTokenService;



    @Transactional
    public AuthenticationResponse authenticateAndGenerateToken(AuthenticationRequest authenticationRequest) {
        UserDetails userDetails = null;
        RefreshToken refreshToken = null;
        String jwt = null;

        try {
            // Tenta autenticar como usuário primeiro
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())
            );
            userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
            User user = userService.findByEmail(authenticationRequest.getEmail());
            refreshToken = refreshTokenService.createRefreshToken(user.getId());

        } catch (AuthenticationException userAuthException) {
            // Se a autenticação do usuário falhar, tenta autenticar como cliente

            try{
                Cliente cliente = clienteRepository.findByEmail(authenticationRequest.getEmail())
                        .orElseThrow(() -> new BadCredentialsException("Cliente não encontrado"));

                if (!authenticationRequest.getPassword().equals(cliente.getCpfCnpj()))
                {
                    throw new BadCredentialsException("Credenciais de cliente inválidas");
                }

                userDetails = clienteDetailsService.loadUserByUsername(authenticationRequest.getEmail());
                refreshToken = refreshTokenService.createRefreshToken(cliente.getId());
            }
            catch(AuthenticationException clientAuthException)
            {
                // Se ambas as autenticações falharem, lança a exceção original do usuário (ou uma nova, se preferir)
                throw new BadCredentialsException("Credenciais inválidas.", userAuthException);
            }


        }
        jwt = jwtUtil.generateToken(userDetails);
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
                        // Trate o caso em que o refresh token não está associado a nenhum usuário/cliente
                        throw new TokenRefreshException(requestRefreshToken, "Refresh token não está associado a nenhum usuário ou cliente.");
                    }

                    String token = jwtUtil.generateToken(userDetails);
                    return new AuthenticationResponse(token, requestRefreshToken);
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh token inválido ou não encontrado."));
    }
}