// src/test/java/com/suportflow/backend/controller/AuthControllerTest.java
package com.suportflow.backend.controller;

import com.suportflow.backend.dto.AuthenticationRequest;
import com.suportflow.backend.dto.AuthenticationResponse;
import com.suportflow.backend.dto.RefreshTokenDTO;
import com.suportflow.backend.exception.TokenRefreshException;
import com.suportflow.backend.service.auth.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAuthenticationToken_Success() {
        // 1. Dados de Entrada
        AuthenticationRequest request = new AuthenticationRequest("test@example.com", "password");
        AuthenticationResponse expectedResponse = new AuthenticationResponse("jwt_token", "refresh_token");

        // 2. Configuração do Mock
        // Quando o authenticationService.authenticateAndGenerateToken() for chamado com *qualquer*
        // objeto AuthenticationRequest, retorne o objeto expectedResponse.
        when(authenticationService.authenticateAndGenerateToken(any(AuthenticationRequest.class)))
                .thenReturn(expectedResponse);

        // 3. Execução do Método sob Teste (do controller)
        ResponseEntity<?> actualResponse = authController.createAuthenticationToken(request);

        // 4. Verificações (Assertions)
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());             // Verifica o status HTTP
        assertEquals(expectedResponse, actualResponse.getBody());               // Verifica o corpo da resposta
        verify(authenticationService, times(1)).authenticateAndGenerateToken(request); //Verifica se foi chamado 1 vez
    }

    @Test
    void testRefreshToken_Success() {
        // 1. Dados de Entrada
        RefreshTokenDTO refreshTokenDTO = new RefreshTokenDTO("valid_refresh_token");
        AuthenticationResponse expectedResponse = new AuthenticationResponse("new_jwt_token", "valid_refresh_token");

        // 2. Configuração do Mock
        when(authenticationService.refreshToken(any(RefreshTokenDTO.class))).thenReturn(expectedResponse);

        // 3. Execução
        ResponseEntity<?> actualResponse = authController.refreshtoken(refreshTokenDTO);

        // 4. Verificações
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(expectedResponse, actualResponse.getBody());
        verify(authenticationService, times(1)).refreshToken(refreshTokenDTO); //Verifica se foi chamado 1 vez

    }
      @Test
        void testCreateAuthenticationToken_InvalidCredentials() {
            AuthenticationRequest request = new AuthenticationRequest("invalid@example.com", "wrong_password");

            when(authenticationService.authenticateAndGenerateToken(request)).thenThrow(new BadCredentialsException("Invalid credentials"));

            ResponseEntity<?> result = authController.createAuthenticationToken(request);

            assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
            assertEquals("Credenciais inválidas.", result.getBody());
            verify(authenticationService, times(1)).authenticateAndGenerateToken(request);
        }

      @Test
      public void testRefreshToken_TokenRefreshException() {
          RefreshTokenDTO refreshTokenDTO = new RefreshTokenDTO("invalid_refresh_token");
          when(authenticationService.refreshToken(refreshTokenDTO)).thenThrow(new TokenRefreshException("invalid_refresh_token", "Refresh token is invalid"));

          ResponseEntity<?> result = authController.refreshtoken(refreshTokenDTO);

          assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
          assertEquals("Failed for [invalid_refresh_token]: Refresh token is invalid", result.getBody()); // Asserção corrigida
          verify(authenticationService, times(1)).refreshToken(refreshTokenDTO);
      }
      @Test
          void testCreateAuthenticationToken_InternalServerError() {
            AuthenticationRequest request = new AuthenticationRequest("error@example.com", "password");

            when(authenticationService.authenticateAndGenerateToken(request)).thenThrow(new RuntimeException("Internal Server Error"));

            ResponseEntity<?> result = authController.createAuthenticationToken(request);

            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
            assertEquals("Erro ao autenticar: Internal Server Error", result.getBody());
            verify(authenticationService, times(1)).authenticateAndGenerateToken(request);
        }


}