// AuthController.java (MODIFICADO)
package com.suportflow.backend.controller;

import com.suportflow.backend.dto.AuthenticationResponse;
import com.suportflow.backend.dto.ClienteLoginDTO;
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

    // Login para USUÁRIOS
    @PostMapping("/login/user")
    public ResponseEntity<?> createAuthenticationTokenUser(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        try {
            AuthenticationResponse response = authenticationService.authenticateAndGenerateToken(userLoginDTO);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais de usuário inválidas.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao autenticar usuário: " + e.getMessage());
        }
    }

    // Login para CLIENTES (NOVO)
    @PostMapping("/login/cliente")
    public ResponseEntity<?> createAuthenticationTokenCliente(@Valid @RequestBody ClienteLoginDTO clienteLoginDTO) {
        try {
            AuthenticationResponse response = authenticationService.authenticateAndGenerateTokenCliente(clienteLoginDTO);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais de cliente inválidas.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao autenticar cliente: " + e.getMessage());
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
}