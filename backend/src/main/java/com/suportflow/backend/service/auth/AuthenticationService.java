// AuthenticationService.java (MODIFICADO)
package com.suportflow.backend.service.auth;

import com.suportflow.backend.dto.AuthenticationResponse;
import com.suportflow.backend.dto.ClienteLoginDTO;
import com.suportflow.backend.dto.RefreshTokenDTO;
import com.suportflow.backend.dto.UserLoginDTO;
import com.suportflow.backend.exception.TokenRefreshException;
import com.suportflow.backend.model.Cliente;
import com.suportflow.backend.model.RefreshToken;
import com.suportflow.backend.model.User;
import com.suportflow.backend.repository.ClienteRepository;
import com.suportflow.backend.security.JwtUtil;
import com.suportflow.backend.service.cliente.ClienteService;
import com.suportflow.backend.service.user.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService; // Para autenticação de usuários
    @Autowired
    private ClienteDetailsService clienteDetailsService; // Para autenticação de clientes
    @Autowired
    private UserManagementService userService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public AuthenticationResponse authenticateAndGenerateToken(UserLoginDTO userLoginDTO) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLoginDTO.getEmail(), userLoginDTO.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Credenciais de usuário inválidas", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(userLoginDTO.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);
        User user = userService.findByEmail(userLoginDTO.getEmail()); // Usar UserManagementService
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId()); // Passar o ID do usuário

        return new AuthenticationResponse(jwt, refreshToken.getToken());
    }

    // Novo método para autenticar CLIENTES
    @Transactional
    public AuthenticationResponse authenticateAndGenerateTokenCliente(ClienteLoginDTO clienteLoginDTO) {
        try {
            // Usa ClienteDetailsService e cpfCnpj como senha
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(clienteLoginDTO.getEmail(), clienteLoginDTO.getCpfCnpj())
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Credenciais de cliente inválidas", e);
        }

        final UserDetails clienteDetails = clienteDetailsService.loadUserByUsername(clienteLoginDTO.getEmail());
        final String jwt = jwtUtil.generateToken(clienteDetails);

        // Buscar o cliente pelo e-mail, e usar seu ID para criar o refresh token
        Cliente cliente = clienteRepository.findByEmail(clienteLoginDTO.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Cliente não encontrado")); // Tratamento adequado

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(cliente.getId()); // Usar o ID do cliente

        return new AuthenticationResponse(jwt, refreshToken.getToken());
    }

    @Transactional
    public AuthenticationResponse refreshToken(RefreshTokenDTO refreshTokenDTO) {
        String requestRefreshToken = refreshTokenDTO.getRefreshToken();

        // Tenta encontrar o refresh token e, a partir dele, o usuário/cliente associado.
        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(refreshToken -> {
                    UserDetails userDetails = null;

                    if (refreshToken.getUser() != null) {
                        userDetails = userDetailsService.loadUserByUsername(refreshToken.getUser().getEmail());
                    }
                    else {
                        userDetails = clienteDetailsService.loadUserByUsername(refreshToken.getCliente().getEmail());
                    }
                    String token = jwtUtil.generateToken(userDetails);
                    return new AuthenticationResponse(token, requestRefreshToken);
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh token inválido ou não encontrado."));
    }
}