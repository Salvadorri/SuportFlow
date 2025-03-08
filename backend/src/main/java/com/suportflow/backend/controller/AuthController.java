// src/main/java/com/suportflow/backend/controller/AuthController.java
package com.suportflow.backend.controller;

import com.suportflow.backend.dto.AuthenticationRequest; // Usando o DTO unificado
import com.suportflow.backend.dto.AuthenticationResponse;
import com.suportflow.backend.dto.RefreshTokenDTO;
import com.suportflow.backend.exception.TokenRefreshException;
import com.suportflow.backend.repository.UserRepository;
import com.suportflow.backend.service.auth.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import com.suportflow.backend.repository.UserRepository;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserRepository userRepository;

    //@Autowired  // REMOVE THIS LINE - AuthenticationRequest is not a bean.
    //private AuthenticationRequest authenticationRequest; // REMOVE THIS LINE

    @PostMapping("/login") // Endpoint unificado
    public ResponseEntity<?> createAuthenticationToken(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        try {
            AuthenticationResponse response = authenticationService.authenticateAndGenerateToken(authenticationRequest);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas."); // Mensagem genérica
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao autenticar: " + e.getMessage());
        }
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody RefreshTokenDTO refreshTokenDTO) {
        try {
            AuthenticationResponse response = authenticationService.refreshToken(refreshTokenDTO);
            return ResponseEntity.ok(response);
        } catch (TokenRefreshException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao renovar o token: " + e.getMessage());
        }
    }
    @PostMapping("/test-user")
    public ResponseEntity<?> testUser(@RequestBody AuthenticationRequest request) {
        boolean userExists = userRepository.existsByEmail(request.getEmail()); // Changed from authenticationRequest to request
        return ResponseEntity.ok("Usuário existe: " + userExists);
    }
}