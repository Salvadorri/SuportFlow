// src/test/java/com/suportflow/backend/service/auth/AuthenticationServiceTest.java
package com.suportflow.backend.service.auth;

import com.suportflow.backend.dto.AuthenticationRequest;
import com.suportflow.backend.dto.AuthenticationResponse;
import com.suportflow.backend.dto.RefreshTokenDTO;
import com.suportflow.backend.exception.TokenRefreshException;
import com.suportflow.backend.model.Cliente;
import com.suportflow.backend.model.RefreshToken;
import com.suportflow.backend.model.User;
import com.suportflow.backend.repository.ClienteRepository;
import com.suportflow.backend.repository.UserRepository;
import com.suportflow.backend.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collections;
import java.time.Instant;



import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


public class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private AuthenticationHelper authenticationHelper;
    @Mock
    private UserDetailsServiceImpl userDetailsService;
    @Mock
    private ClienteDetailsService clienteDetailsService;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private RefreshTokenService refreshTokenService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ClienteRepository clienteRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAuthenticateAndGenerateToken_UserSuccess() {
        // 1. Dados de Entrada
        AuthenticationRequest request = new AuthenticationRequest("test@example.com", "password");

        // 2. Mocks Intermediários (objetos que serão retornados pelos mocks)
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("test@example.com");
        mockUser.setSenha("hashed_password"); // Senha *já criptografada*
        mockUser.setAtivo(true);  // Importante!
        //UserDetails para User (não precisa de permissões específicas no teste de sucesso)
        UserDetails mockUserDetails = org.springframework.security.core.userdetails.User.withUsername("test@example.com")
                .password("hashed_password").authorities(Collections.emptyList()).build();

        RefreshToken mockRefreshToken = new RefreshToken();
        mockRefreshToken.setToken("refresh_token");

        // 3. Configuração dos Mocks
        when(authenticationHelper.authenticateUser(request.getEmail(), request.getPassword()))
                .thenReturn(mockUserDetails);  // Retorna UserDetails

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(mockUser)); // Retorna o User
        when(refreshTokenService.createRefreshToken(1L)).thenReturn(mockRefreshToken);
        when(jwtUtil.generateToken(mockUserDetails, 1L)).thenReturn("jwt_token");


        // 4. Execução do Método sob Teste
        AuthenticationResponse response = authenticationService.authenticateAndGenerateToken(request);

        // 5. Verificações
        assertNotNull(response);
        assertEquals("jwt_token", response.getJwt());
        assertEquals("refresh_token", response.getRefreshToken());

        //Verificações de chamada
        verify(authenticationHelper, times(1)).authenticateUser(request.getEmail(),request.getPassword());
        verify(userRepository, times(1)).findByEmail(request.getEmail());
        verify(refreshTokenService, times(1)).createRefreshToken(1L);
        verify(jwtUtil,times(1)).generateToken(mockUserDetails, 1L);
    }

@Test
void testAuthenticateAndGenerateToken_UserNotFound() {
    AuthenticationRequest request = new AuthenticationRequest("nonexistent@example.com", "password");

    // Simula que o usuário não é encontrado
    when(authenticationHelper.authenticateUser(request.getEmail(), request.getPassword()))
        .thenThrow(new UsernameNotFoundException("User not found"));
     when(authenticationHelper.authenticateCliente(request.getEmail(), request.getPassword()))
            .thenThrow(new UsernameNotFoundException("Cliente not found"));

    // Verifica se a exceção correta é lançada
    assertThrows(BadCredentialsException.class, () -> authenticationService.authenticateAndGenerateToken(request));
    verify(authenticationHelper, times(1)).authenticateUser(request.getEmail(), request.getPassword());
    verify(authenticationHelper, times(1)).authenticateCliente(request.getEmail(),request.getPassword());

}

@Test
void testAuthenticateAndGenerateToken_InvalidUserCredentials() {
    AuthenticationRequest request = new AuthenticationRequest("test@example.com", "wrong_password");

    // Simula credenciais inválidas
    when(authenticationHelper.authenticateUser(request.getEmail(), request.getPassword()))
        .thenThrow(new BadCredentialsException("Invalid credentials"));
      when(authenticationHelper.authenticateCliente(request.getEmail(), request.getPassword()))
                .thenThrow(new BadCredentialsException("Invalid credentials"));


    // Verifica se a exceção correta é lançada
    assertThrows(BadCredentialsException.class, () -> authenticationService.authenticateAndGenerateToken(request));
    verify(authenticationHelper, times(1)).authenticateUser(request.getEmail(), request.getPassword());
     verify(authenticationHelper, times(1)).authenticateCliente(request.getEmail(),request.getPassword());

}

//Mesma coisa para Cliente, mudando o repository e o authenticationHelper
@Test
void testAuthenticateAndGenerateToken_ClienteNotFound() {
    AuthenticationRequest request = new AuthenticationRequest("nonexistent_cliente@example.com", "password");

    // Simula que authenticateUser falha
    when(authenticationHelper.authenticateUser(request.getEmail(), request.getPassword()))
            .thenThrow(new UsernameNotFoundException("User not found"));
    // Simula que o cliente não é encontrado
    when(authenticationHelper.authenticateCliente(request.getEmail(), request.getPassword()))
            .thenThrow(new UsernameNotFoundException("Cliente not found"));


    assertThrows(BadCredentialsException.class, () -> authenticationService.authenticateAndGenerateToken(request));
     verify(authenticationHelper, times(1)).authenticateUser(request.getEmail(), request.getPassword());
     verify(authenticationHelper, times(1)).authenticateCliente(request.getEmail(),request.getPassword());

}

@Test
void testAuthenticateAndGenerateToken_InvalidClienteCredentials() {
    AuthenticationRequest request = new AuthenticationRequest("cliente@example.com", "wrong_password");

    // Simula que authenticateUser falha
    when(authenticationHelper.authenticateUser(request.getEmail(), request.getPassword()))
            .thenThrow(new UsernameNotFoundException("User not found"));

    // Simula credenciais inválidas do cliente
    when(authenticationHelper.authenticateCliente(request.getEmail(), request.getPassword()))
            .thenThrow(new BadCredentialsException("Invalid client credentials"));

     assertThrows(BadCredentialsException.class, () -> authenticationService.authenticateAndGenerateToken(request));
    verify(authenticationHelper, times(1)).authenticateUser(request.getEmail(), request.getPassword());
    verify(authenticationHelper, times(1)).authenticateCliente(request.getEmail(),request.getPassword());


}
// Em AuthenticationServiceTest.java

@Test
void testRefreshToken_Success_User() {
    RefreshTokenDTO refreshTokenDTO = new RefreshTokenDTO("valid_user_refresh_token");

    // Mocks
    RefreshToken mockRefreshToken = new RefreshToken();
    User mockUser = new User();
    mockUser.setId(1L);
    mockUser.setEmail("test@example.com");
    mockRefreshToken.setUser(mockUser);
    mockRefreshToken.setToken("valid_user_refresh_token");
    mockRefreshToken.setExpiryDate(Instant.now().plusSeconds(3600)); // Token válido

     UserDetails mockUserDetails = org.springframework.security.core.userdetails.User.withUsername("test@example.com")
            .password("hashed_password").authorities(Collections.emptyList()).build();

    // Configuração dos mocks
    when(refreshTokenService.findByToken("valid_user_refresh_token")).thenReturn(Optional.of(mockRefreshToken));
    when(refreshTokenService.verifyExpiration(mockRefreshToken)).thenReturn(mockRefreshToken);
    when(userDetailsService.loadUserByUsername("test@example.com")).thenReturn(mockUserDetails); // Retorna UserDetails
    when(jwtUtil.generateToken(mockUserDetails, 1L)).thenReturn("new_jwt_token");


    // Execução
    AuthenticationResponse response = authenticationService.refreshToken(refreshTokenDTO);

    // Verificações
    assertNotNull(response);
    assertEquals("new_jwt_token", response.getJwt());
    assertEquals("valid_user_refresh_token", response.getRefreshToken()); // Deve retornar o *mesmo* refresh token
    verify(refreshTokenService, times(1)).findByToken(refreshTokenDTO.getRefreshToken());
    verify(refreshTokenService, times(1)).verifyExpiration(mockRefreshToken);
    verify(userDetailsService, times(1)).loadUserByUsername(mockUser.getEmail());
    verify(jwtUtil, times(1)).generateToken(mockUserDetails, 1L);

}

@Test
void testRefreshToken_Success_Cliente() {
    RefreshTokenDTO refreshTokenDTO = new RefreshTokenDTO("valid_cliente_refresh_token");

    // Mocks
    RefreshToken mockRefreshToken = new RefreshToken();
    Cliente mockCliente = new Cliente();
    mockCliente.setId(2L);
    mockCliente.setEmail("cliente@example.com");
    mockRefreshToken.setCliente(mockCliente);
    mockRefreshToken.setToken("valid_cliente_refresh_token");
    mockRefreshToken.setExpiryDate(Instant.now().plusSeconds(3600)); // Token válido

      UserDetails mockClienteUserDetails = org.springframework.security.core.userdetails.User.withUsername("cliente@example.com")
                .password("hashed_password").authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_CLIENTE"))).build();


    // Configuração
    when(refreshTokenService.findByToken("valid_cliente_refresh_token")).thenReturn(Optional.of(mockRefreshToken));
    when(refreshTokenService.verifyExpiration(mockRefreshToken)).thenReturn(mockRefreshToken);
    when(clienteDetailsService.loadUserByUsername("cliente@example.com")).thenReturn(mockClienteUserDetails); // Retorna UserDetails do Cliente
    when(jwtUtil.generateToken(mockClienteUserDetails, 2L)).thenReturn("new_jwt_token_cliente");

    // Execução
    AuthenticationResponse response = authenticationService.refreshToken(refreshTokenDTO);

    // Verificações
    assertNotNull(response);
    assertEquals("new_jwt_token_cliente", response.getJwt());
    assertEquals("valid_cliente_refresh_token", response.getRefreshToken());

    verify(refreshTokenService, times(1)).findByToken(refreshTokenDTO.getRefreshToken());
    verify(refreshTokenService, times(1)).verifyExpiration(mockRefreshToken);
    verify(clienteDetailsService, times(1)).loadUserByUsername(mockCliente.getEmail());
    verify(jwtUtil, times(1)).generateToken(mockClienteUserDetails, 2L);
}
// Em AuthenticationServiceTest.java
@Test
void testRefreshToken_InvalidToken() {
    RefreshTokenDTO refreshTokenDTO = new RefreshTokenDTO("invalid_refresh_token");

    // Simula que o token não é encontrado
    when(refreshTokenService.findByToken("invalid_refresh_token")).thenReturn(Optional.empty());

    // Verifica se a exceção correta é lançada
    assertThrows(TokenRefreshException.class, () -> authenticationService.refreshToken(refreshTokenDTO));
    verify(refreshTokenService, times(1)).findByToken(refreshTokenDTO.getRefreshToken());
}

@Test
void testRefreshToken_ExpiredToken() {
    RefreshTokenDTO refreshTokenDTO = new RefreshTokenDTO("expired_refresh_token");
    RefreshToken mockRefreshToken = new RefreshToken();
    mockRefreshToken.setToken("expired_refresh_token");

    // Simula que o token é encontrado, mas está expirado
    when(refreshTokenService.findByToken("expired_refresh_token")).thenReturn(Optional.of(mockRefreshToken));
    when(refreshTokenService.verifyExpiration(mockRefreshToken)).thenThrow(new TokenRefreshException("expired_refresh_token", "Refresh token expired"));

    // Verifica se a exceção correta é lançada
    assertThrows(TokenRefreshException.class, () -> authenticationService.refreshToken(refreshTokenDTO));
     verify(refreshTokenService, times(1)).findByToken(refreshTokenDTO.getRefreshToken());

}

@Test
void testRefreshToken_NoUserOrClient() {
    RefreshTokenDTO refreshTokenDTO = new RefreshTokenDTO("no_user_client_token");
    RefreshToken mockRefreshToken = new RefreshToken();
    mockRefreshToken.setToken("no_user_client_token");
    mockRefreshToken.setExpiryDate(Instant.now().plusSeconds(3600)); // Token válido

    // Simula que o token é encontrado, mas não está associado a nenhum usuário/cliente
    when(refreshTokenService.findByToken("no_user_client_token")).thenReturn(Optional.of(mockRefreshToken));
    when(refreshTokenService.verifyExpiration(mockRefreshToken)).thenReturn(mockRefreshToken);

    // Verifica se a exceção correta é lançada
    assertThrows(TokenRefreshException.class, () -> authenticationService.refreshToken(refreshTokenDTO));
     verify(refreshTokenService, times(1)).findByToken(refreshTokenDTO.getRefreshToken());
      verify(refreshTokenService, times(1)).verifyExpiration(mockRefreshToken);


}
@Test
void testAuthenticateAndGenerateToken_ClienteSuccess_JwtHasClienteRole() {
    // 1. Dados de Entrada
    AuthenticationRequest request = new AuthenticationRequest("cliente@example.com", "password");

    // 2. Mocks Intermediários
    Cliente mockCliente = new Cliente();
    mockCliente.setId(2L);
    mockCliente.setEmail("cliente@example.com");
    mockCliente.setSenha("hashed_password");
    mockCliente.setAtivo(true);

    UserDetails mockClienteUserDetails = org.springframework.security.core.userdetails.User.withUsername("cliente@example.com")
            .password("hashed_password").authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_CLIENTE"))).build();

    RefreshToken mockRefreshToken = new RefreshToken();
    mockRefreshToken.setToken("refresh_token_cliente");

    // 3. Configuração dos Mocks
    when(authenticationHelper.authenticateUser(request.getEmail(), request.getPassword()))
            .thenThrow(new UsernameNotFoundException("User not found"));
    when(authenticationHelper.authenticateCliente(request.getEmail(), request.getPassword()))
            .thenReturn(mockClienteUserDetails);
    when(clienteRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(mockCliente));
    when(refreshTokenService.createRefreshToken(2L)).thenReturn(mockRefreshToken);

    // Define o que o jwtUtil.generateToken deve retornar (um token de exemplo)
    String jwtToken = "fake.jwt.token";
    when(jwtUtil.generateToken(mockClienteUserDetails, 2L)).thenReturn(jwtToken);


    // 4. Execução
    AuthenticationResponse response = authenticationService.authenticateAndGenerateToken(request);

    // 5. Verificações
    assertNotNull(response);
    assertEquals(jwtToken, response.getJwt());
    assertEquals("refresh_token_cliente", response.getRefreshToken());

    verify(jwtUtil, never()).extractRoles(jwtToken); //Garante que não tentamos extrair roles, que é só para usuários
    verify(authenticationHelper, times(1)).authenticateUser(request.getEmail(), request.getPassword());
    verify(authenticationHelper, times(1)).authenticateCliente(request.getEmail(), request.getPassword());
    verify(clienteRepository, times(1)).findByEmail(request.getEmail());
    verify(refreshTokenService, times(1)).createRefreshToken(2L);
    verify(jwtUtil, times(1)).generateToken(mockClienteUserDetails, 2L);
}

}