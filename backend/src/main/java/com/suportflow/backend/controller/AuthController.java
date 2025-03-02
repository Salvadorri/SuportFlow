package com.suportflow.backend.controller;

import com.suportflow.backend.dto.*;
import com.suportflow.backend.exception.TokenRefreshException;
import com.suportflow.backend.service.auth.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth") // Prefixo /api/auth para autenticação
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        try {
            AuthenticationResponse response = authenticationService.authenticateAndGenerateToken(userLoginDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) { // Captura exceções mais genéricas do serviço
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas.");
        }
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody RefreshTokenDTO refreshTokenDTO) {
       try {
           AuthenticationResponse response = authenticationService.refreshToken(refreshTokenDTO);
           return ResponseEntity.ok(response);
       }catch (TokenRefreshException e){
           return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
       }

    }
}