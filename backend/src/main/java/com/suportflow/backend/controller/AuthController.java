package com.suportflow.backend.controller;

import com.suportflow.backend.dto.AuthenticationResponse;
import com.suportflow.backend.dto.RefreshTokenDTO;
import com.suportflow.backend.dto.UserLoginDTO;
import com.suportflow.backend.exception.TokenRefreshException;
import com.suportflow.backend.service.auth.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        try {
            AuthenticationResponse response = authenticationService.authenticateAndGenerateToken(userLoginDTO);
            return ResponseEntity.ok(response); // Retorna AuthenticationResponse (com JWT e refreshToken)
        } catch (BadCredentialsException e) {
            // Captura especificamente BadCredentialsException
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas.");
        } catch (Exception e) {
            // Captura outras exceções (improvável, mas bom ter)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao autenticar: " + e.getMessage());
        }
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody RefreshTokenDTO refreshTokenDTO) {
        try {
            AuthenticationResponse response = authenticationService.refreshToken(refreshTokenDTO);
            return ResponseEntity.ok(response); // Retorna AuthenticationResponse (com novo JWT)
        } catch (TokenRefreshException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            // Captura outras exceções (improvável, mas bom ter)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao renovar o token: " + e.getMessage());
        }
    }
}