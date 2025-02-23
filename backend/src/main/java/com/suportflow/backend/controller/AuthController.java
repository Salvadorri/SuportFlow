package com.suportflow.backend.controller;

import com.suportflow.backend.dto.*;
import com.suportflow.backend.exception.TokenRefreshException;
import com.suportflow.backend.model.RefreshToken;
import com.suportflow.backend.model.User;
import com.suportflow.backend.security.JwtUtil;
import com.suportflow.backend.service.auth.RefreshTokenService;
import com.suportflow.backend.service.auth.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api") // Prefixo base para todos os endpoints neste controlador
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @PostMapping("/auth/login") // Mantém o /login em /api/auth
    public ResponseEntity<?> createAuthenticationToken(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLoginDTO.getEmail(), userLoginDTO.getPassword())
            );
        } catch (BadCredentialsException e) {
            // Usar um logger em produção!
            System.err.println("Credenciais inválidas: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas.");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(userLoginDTO.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);

        // Criar e salvar o refresh token
        User user = userService.findByEmail(userLoginDTO.getEmail());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        return ResponseEntity.ok(new AuthenticationResponse(jwt, refreshToken.getToken()));
    }


    // Novo endpoint para registro: /api/users/register
    @PostMapping("/users/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDTO registrationDTO) {
        try {
            UserDetailsDTO registeredUser = userService.registerNewUser(registrationDTO);
            return ResponseEntity.ok(registeredUser);
        } catch (DataIntegrityViolationException e) {
            // Trata erro de e-mail duplicado
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Já existe um usuário com este e-mail.");
        } catch (Exception e) {
            // Trata outros erros genéricos (logar o erro é importante)
            System.err.println("Erro ao registrar usuário: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao registrar usuário.");
        }
    }


    @PostMapping("/auth/refreshtoken") // Mantém o /refreshtoken em /api/auth
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody RefreshTokenDTO refreshTokenDTO) {
        String requestRefreshToken = refreshTokenDTO.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
                    String token = jwtUtil.generateToken(userDetails);
                    return ResponseEntity.ok(new AuthenticationResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh token inválido ou não encontrado."));
    }
}