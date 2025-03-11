package com.suportflow.backend.controller;

import com.suportflow.backend.dto.*;
import com.suportflow.backend.exception.UserNotFoundException;
import com.suportflow.backend.service.user.UserManagementService;
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

public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserManagementService userManagementService;

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
    private UserRegistrationDTO createValidRegistrationDTO() {
        UserRegistrationDTO dto = new UserRegistrationDTO();
        dto.setNome("New User");
        dto.setEmail("newuser@example.com");
        dto.setPassword("password");
        dto.setTelefone("11999999999");
        dto.setCpfCnpj("12345678901");
        dto.setRoles(Arrays.asList("USER")); // Assuming USER role exists
        return dto;
    }

    private UserDetailsDTO createUserDetailsDTO(String email) {
        UserDetailsDTO dto = new UserDetailsDTO();
        dto.setEmail(email);
        dto.setNome("Test User");
        dto.setId(1L);
        dto.setDataCriacao(LocalDateTime.now());
        return dto;
    }

    private UserUpdateDTO createValidUpdateDTO() {
        UserUpdateDTO dto = new UserUpdateDTO();
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
    void testRegisterUser_Success_ReturnsCreatedUserDetailsDTO() {
        UserRegistrationDTO registrationDTO = createValidRegistrationDTO();
        UserDetailsDTO expectedUser = createUserDetailsDTO(registrationDTO.getEmail());

        when(userManagementService.registerNewUser(registrationDTO)).thenReturn(expectedUser);

        ResponseEntity<?> response = userController.registerUser(registrationDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedUser, response.getBody());
        verify(userManagementService, times(1)).registerNewUser(registrationDTO);
    }

    @Test
    void testRegisterUser_Error_ThrowsException() {
        UserRegistrationDTO registrationDTO = createValidRegistrationDTO();
        when(userManagementService.registerNewUser(registrationDTO)).thenThrow(new RuntimeException("Registration error"));

        ResponseEntity<?> response = userController.registerUser(registrationDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Erro ao registrar usuário: Registration error", response.getBody());
        verify(userManagementService, times(1)).registerNewUser(registrationDTO);
    }

    @Test
    void testGetCurrentUser_Success_ReturnsUserDetailsDTO() {
        UserDetailsDTO expectedUser = createUserDetailsDTO(TEST_EMAIL);

        when(userManagementService.findDTOByEmail(TEST_EMAIL)).thenReturn(expectedUser);

        ResponseEntity<UserDetailsDTO> response = userController.getCurrentUser();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedUser, response.getBody());
        verify(userManagementService, times(1)).findDTOByEmail(TEST_EMAIL);
    }

    @Test
    void testUpdateCurrentUser_Success_ReturnsUpdatedUserDetailsDTO() {
        UserUpdateDTO updateDTO = createValidUpdateDTO();
        UserDetailsDTO updatedUser = createUserDetailsDTO(TEST_EMAIL);
        updatedUser.setNome(updateDTO.getNome());

        when(userManagementService.updateUserByEmail(TEST_EMAIL, updateDTO)).thenReturn(updatedUser);

        ResponseEntity<?> response = userController.updateCurrentUser(updateDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedUser, response.getBody());
        verify(userManagementService, times(1)).updateUserByEmail(TEST_EMAIL, updateDTO);
    }

    @Test
    void testChangePassword_Success_ReturnsOk() {
        PasswordChangeDTO passwordChangeDTO = createValidPasswordChangeDTO();

        ResponseEntity<?> response = userController.changePassword(passwordChangeDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Senha alterada com sucesso.", response.getBody());
        verify(userManagementService, times(1)).changePassword(TEST_EMAIL, passwordChangeDTO);
    }

    @Test
    void testGetUserById_Success_ReturnsUserDetailsDTO() {
        UserDetailsDTO user = createUserDetailsDTO("user@example.com");
        user.setId(1L);

        when(userManagementService.findDTOById(1L)).thenReturn(user);

        ResponseEntity<UserDetailsDTO> response = userController.getUserById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
        verify(userManagementService, times(1)).findDTOById(1L);
    }

    @Test
    void testGetAllUsers_Success_ReturnsListOfUserDetailsDTOs() {
        List<UserDetailsDTO> users = Arrays.asList(createUserDetailsDTO("user1@example.com"), createUserDetailsDTO("user2@example.com"));
        when(userManagementService.findAllUsers()).thenReturn(users);

        ResponseEntity<List<UserDetailsDTO>> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
        verify(userManagementService, times(1)).findAllUsers();
    }

    @Test
    void testUpdateUser_Success_ReturnsUpdatedUserDetailsDTO() {
        UserUpdateDTO updateDTO = createValidUpdateDTO();
        UserDetailsDTO updatedUser = createUserDetailsDTO("user@example.com");
        updatedUser.setNome(updateDTO.getNome());

        when(userManagementService.updateUser(eq(1L), any(UserUpdateDTO.class))).thenReturn(updatedUser);

        ResponseEntity<?> response = userController.updateUser(1L, updateDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedUser, response.getBody());
        verify(userManagementService, times(1)).updateUser(eq(1L), any(UserUpdateDTO.class));
    }

    @Test
    void testDeleteUser_Success_ReturnsNoContent() {
        ResponseEntity<?> response = userController.deleteUser(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userManagementService, times(1)).deleteUser(1L);
    }

    @Test
    void testUpdateCurrentUser_UserNotFound_ReturnsNotFound() {
        UserUpdateDTO updateDTO = createValidUpdateDTO();

        when(userManagementService.updateUserByEmail(TEST_EMAIL, updateDTO)).thenThrow(new UserNotFoundException("Usuário não encontrado"));

        ResponseEntity<?> response = userController.updateCurrentUser(updateDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Usuário não encontrado", response.getBody());
        verify(userManagementService, times(1)).updateUserByEmail(TEST_EMAIL, updateDTO);
    }


}