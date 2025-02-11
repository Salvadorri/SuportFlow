// file:///home/rodrigo/Desenvolvimento/SuportFlow/backend/src/main/java/com/suportflow/backend/controller/AuthController.java
package com.suportflow.backend.controller;

import com.suportflow.backend.dto.*;
import com.suportflow.backend.exception.TokenRefreshException;
import com.suportflow.backend.model.RefreshToken;
import com.suportflow.backend.model.User;
import com.suportflow.backend.security.JwtUtil;
import com.suportflow.backend.service.auth.RefreshTokenService;
import com.suportflow.backend.service.auth.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
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

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody UserLoginDTO userLoginDTO) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLoginDTO.getEmail(), userLoginDTO.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Email ou senha incorretos.", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(userLoginDTO.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);

        // Criar e salvar o refresh token
        User user = userService.findByEmail(userLoginDTO.getEmail());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        return ResponseEntity.ok(new AuthenticationResponse(jwt, refreshToken.getToken()));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationDTO registrationDTO) {
        // 1. Verificar se o usuário está autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autenticado.");
        }

        // 2. Verificar se o usuário tem a permissão 'CREATE_USER'
        boolean hasCreateUserPermission = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> authority.equals("CREATE_USER"));

        if (!hasCreateUserPermission) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Usuário não tem permissão para registrar novos usuários.");
        }

        // 3. Se chegou aqui, o usuário está autenticado e tem a permissão necessária
        // Registra o novo usuário
        UserDetailsDTO registeredUser = userService.registerNewUser(registrationDTO);

        // 4. Retorna o usuário registrado na resposta (ou um status de sucesso)
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@RequestBody RefreshTokenDTO refreshTokenDTO) {
        String requestRefreshToken = refreshTokenDTO.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
                    String token = jwtUtil.generateToken(userDetails);
                    return ResponseEntity.ok(new AuthenticationResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh token is not in database!"));
    }
}