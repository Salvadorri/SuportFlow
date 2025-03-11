package com.suportflow.backend.service.auth;

import com.suportflow.backend.model.Cliente;
import com.suportflow.backend.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteDetailsServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteDetailsService clienteDetailsService;

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setEmail("test@example.com");
        cliente.setSenha("password");
        cliente.setAtivo(true);
    }

    @Test
    void loadUserByUsername_ExistingUser_ReturnsUserDetails() {
        when(clienteRepository.findByEmail("test@example.com")).thenReturn(Optional.of(cliente));

        UserDetails userDetails = clienteDetailsService.loadUserByUsername("test@example.com");

        assertNotNull(userDetails);
        assertEquals(cliente.getEmail(), userDetails.getUsername());
        assertEquals(cliente.getSenha(), userDetails.getPassword());
        assertTrue(userDetails.isEnabled());
        verify(clienteRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void loadUserByUsername_NonExistingUser_ThrowsUsernameNotFoundException() {
        when(clienteRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            clienteDetailsService.loadUserByUsername("test@example.com");
        });

        verify(clienteRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void loadUserByUsername_UserInactive_ReturnsUserDetailsWithCorrectStatus() {
        cliente.setAtivo(false);
        when(clienteRepository.findByEmail("test@example.com")).thenReturn(Optional.of(cliente));

        UserDetails userDetails = clienteDetailsService.loadUserByUsername("test@example.com");

        assertNotNull(userDetails);
        assertFalse(userDetails.isEnabled());
        verify(clienteRepository, times(1)).findByEmail("test@example.com");
    }
}