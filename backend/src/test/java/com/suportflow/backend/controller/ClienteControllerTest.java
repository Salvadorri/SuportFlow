package com.suportflow.backend.controller;

import com.suportflow.backend.dto.ClienteDTO;
import com.suportflow.backend.dto.ClienteRegistrationDTO;
import com.suportflow.backend.dto.ClienteUpdateDTO;
import com.suportflow.backend.dto.PasswordChangeDTO;
import com.suportflow.backend.exception.UniqueFieldAlreadyExistsException;
import com.suportflow.backend.exception.UserNotFoundException;
import com.suportflow.backend.service.cliente.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ClienteControllerTest {

    @InjectMocks
    private ClienteController clienteController;

    @Mock
    private ClienteService clienteService;

    @Mock
    private Authentication authentication;

    private final String TEST_EMAIL = "test@example.com";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getName()).thenReturn(TEST_EMAIL);
    }

    // Helper methods to create test data
    private ClienteRegistrationDTO createValidRegistrationDTO() {
        ClienteRegistrationDTO dto = new ClienteRegistrationDTO();
        dto.setNome("New Client");
        dto.setEmail("newclient@example.com");
        dto.setTelefone("11999999999");
        dto.setCpfCnpj("12345678901");
        dto.setSenha("password");
        return dto;
    }

    private ClienteDTO createClienteDTO(String email) {
        ClienteDTO dto = new ClienteDTO();
        dto.setEmail(email);
        dto.setNome("Test Client");
        dto.setId(1L);
        dto.setDataCadastro(LocalDateTime.now());
        return dto;
    }

    private ClienteUpdateDTO createValidUpdateDTO() {
        ClienteUpdateDTO dto = new ClienteUpdateDTO();
        dto.setNome("Updated Name");
        dto.setTelefone("21999999999");
        return dto;
    }

    private PasswordChangeDTO createValidPasswordChangeDTO() {
        PasswordChangeDTO dto = new PasswordChangeDTO();
        dto.setOldPassword("oldPassword");
        dto.setNewPassword("newPassword");
        dto.setConfirmNewPassword("newPassword");
        return dto;
    }

    @Test
    void testRegisterCliente_Success_ReturnsCreatedCliente() {
        ClienteRegistrationDTO registrationDTO = createValidRegistrationDTO();
        ClienteDTO expectedCliente = createClienteDTO(registrationDTO.getEmail());

        when(clienteService.save(registrationDTO)).thenReturn(expectedCliente);

        ResponseEntity<?> response = clienteController.registerCliente(registrationDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedCliente, response.getBody());
        verify(clienteService, times(1)).save(registrationDTO);
    }

    @Test
    void testRegisterCliente_Error_ThrowsException() {
        ClienteRegistrationDTO registrationDTO = createValidRegistrationDTO();
        when(clienteService.save(registrationDTO)).thenThrow(new RuntimeException("Registration error"));

        ResponseEntity<?> response = clienteController.registerCliente(registrationDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Erro ao registrar cliente: Registration error", response.getBody());
        verify(clienteService, times(1)).save(registrationDTO);
    }

    @Test
    void testGetCurrentCliente_Success_ReturnsClienteDTO() {
        ClienteDTO expectedCliente = createClienteDTO(TEST_EMAIL);

        when(clienteService.findDTOByEmail(TEST_EMAIL)).thenReturn(expectedCliente);

        ResponseEntity<ClienteDTO> response = clienteController.getCurrentCliente();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedCliente, response.getBody());
        verify(clienteService, times(1)).findDTOByEmail(TEST_EMAIL);
    }

    @Test
    void testUpdateCurrentCliente_Success_ReturnsUpdatedClienteDTO() {
        ClienteUpdateDTO updateDTO = createValidUpdateDTO();
        ClienteDTO updatedCliente = createClienteDTO(TEST_EMAIL);
        updatedCliente.setNome(updateDTO.getNome());

        when(clienteService.updateByEmail(TEST_EMAIL, updateDTO)).thenReturn(updatedCliente);

        ResponseEntity<?> response = clienteController.updateCurrentCliente(updateDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedCliente, response.getBody());
        verify(clienteService, times(1)).updateByEmail(TEST_EMAIL, updateDTO);
    }

    @Test
    void testChangeCurrentClientePassword_Success_ReturnsOk() {
        PasswordChangeDTO passwordChangeDTO = createValidPasswordChangeDTO();

        ResponseEntity<?> response = clienteController.changeCurrentClientePassword(passwordChangeDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Senha alterada com sucesso.", response.getBody());
        verify(clienteService, times(1)).changePasswordByEmail(TEST_EMAIL, passwordChangeDTO);
    }

    @Test
    void testGetAllClientes_Success_ReturnsListOfClienteDTOs() {
        List<ClienteDTO> clientes = Arrays.asList(createClienteDTO("client1@example.com"), createClienteDTO("client2@example.com"));
        when(clienteService.findAllDTO()).thenReturn(clientes);

        ResponseEntity<List<ClienteDTO>> response = clienteController.getAllClientes();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(clientes, response.getBody());
        verify(clienteService, times(1)).findAllDTO();
    }

    @Test
    void testGetClienteById_Success_ReturnsClienteDTO() {
        ClienteDTO cliente = createClienteDTO("client@example.com");
        cliente.setId(1L);

        when(clienteService.findDTOById(1L)).thenReturn(cliente);

        ResponseEntity<ClienteDTO> response = clienteController.getClienteById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cliente, response.getBody());
        verify(clienteService, times(1)).findDTOById(1L);
    }

    @Test
    void testUpdateCliente_Success_ReturnsUpdatedClienteDTO() {
        ClienteUpdateDTO updateDTO = createValidUpdateDTO();
        ClienteDTO updatedCliente = createClienteDTO("client@example.com");
        updatedCliente.setNome(updateDTO.getNome());

        when(clienteService.update(eq(1L), any(ClienteUpdateDTO.class))).thenReturn(updatedCliente);

        ResponseEntity<?> response = clienteController.updateCliente(1L, updateDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedCliente, response.getBody());
        verify(clienteService, times(1)).update(eq(1L), any(ClienteUpdateDTO.class));
    }

    @Test
    void testDeleteCliente_Success_ReturnsNoContent() {
        ResponseEntity<Void> response = clienteController.deleteCliente(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(clienteService, times(1)).delete(1L);
    }


    @Test
    void testUpdateCurrentCliente_UserNotFound_ReturnsNotFound() {
        ClienteUpdateDTO updateDTO = createValidUpdateDTO();

        when(clienteService.updateByEmail(TEST_EMAIL, updateDTO)).thenThrow(new UserNotFoundException("Cliente não encontrado"));

        ResponseEntity<?> response = clienteController.updateCurrentCliente(updateDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Cliente não encontrado", response.getBody());
        verify(clienteService, times(1)).updateByEmail(TEST_EMAIL, updateDTO);
    }

    @Test
    void testRegisterCliente_DuplicateEmail_ReturnsConflict() {
        ClienteRegistrationDTO registrationDTO = createValidRegistrationDTO();
        when(clienteService.save(registrationDTO)).thenThrow(new UniqueFieldAlreadyExistsException("Já existe um cliente com este email."));

        ResponseEntity<?> response = clienteController.registerCliente(registrationDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Erro ao registrar cliente: Já existe um cliente com este email.", response.getBody());
        verify(clienteService, times(1)).save(registrationDTO);
    }
}