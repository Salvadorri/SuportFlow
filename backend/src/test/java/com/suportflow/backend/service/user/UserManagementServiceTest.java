package com.suportflow.backend.service.user;// backend/src/test/java/com/suportflow/backend/service/user/UserManagementServiceTest.java
import com.suportflow.backend.dto.PasswordChangeDTO;
import com.suportflow.backend.dto.UserDetailsDTO;
import com.suportflow.backend.dto.UserRegistrationDTO;
import com.suportflow.backend.dto.UserUpdateDTO;
import com.suportflow.backend.exception.UserNotFoundException;
import com.suportflow.backend.model.Empresa;
import com.suportflow.backend.model.Permissao;
import com.suportflow.backend.model.User;
import com.suportflow.backend.repository.EmpresaRepository;
import com.suportflow.backend.repository.PermissaoRepository;
import com.suportflow.backend.repository.UserRepository;
import com.suportflow.backend.service.user.UserManagementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserManagementServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmpresaRepository empresaRepository;

    @Mock
    private PermissaoRepository permissaoRepository;

    @InjectMocks
    private UserManagementService userManagementService;

    private User testUser;
    private Empresa testEmpresa;
    private Permissao testPermissao;

    @BeforeEach
    void setUp() {
        testEmpresa = new Empresa();
        testEmpresa.setId(1L);
        testEmpresa.setNome("Test Company");
        testEmpresa.setEmail("test@company.com");

        testPermissao = new Permissao();
        testPermissao.setId(1L);
        testPermissao.setNome("ROLE_USER");

        testUser = new User();
        testUser.setId(1L);
        testUser.setNome("Test User");
        testUser.setEmail("test@example.com");
        testUser.setSenha("encodedPassword");
        testUser.setEmpresa(testEmpresa);
        testUser.setAtivo(true);
        testUser.setDataCriacao(LocalDateTime.now());
        testUser.setTelefone("123-456-7890");
        testUser.setCpfCnpj("12345678900");
        testUser.setPermissoes(Collections.singleton(testPermissao));
    }

    @Test
    void registerNewUser_success() {
        UserRegistrationDTO registrationDTO = new UserRegistrationDTO();
        registrationDTO.setNome("New User");
        registrationDTO.setEmail("new@example.com");
        registrationDTO.setPassword("password");
        registrationDTO.setEmpresaNome("Test Company");
        registrationDTO.setTelefone("098-765-4321");
        registrationDTO.setCpfCnpj("00987654321");
        registrationDTO.setRoles(Collections.singletonList("ROLE_USER"));

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByCpfCnpj(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(empresaRepository.findByNome(anyString())).thenReturn(Optional.of(testEmpresa));
        when(permissaoRepository.findByNome(anyString())).thenReturn(Optional.of(testPermissao));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        UserDetailsDTO userDetailsDTO = userManagementService.registerNewUser(registrationDTO);

        assertNotNull(userDetailsDTO);
        assertEquals("Test User", userDetailsDTO.getNome());
        assertEquals("test@example.com", userDetailsDTO.getEmail());
        assertEquals("Test Company", userDetailsDTO.getEmpresaNome());
        assertEquals("123-456-7890", userDetailsDTO.getTelefone());
        assertEquals("12345678900", userDetailsDTO.getCpfCnpj());
        assertEquals(1, userDetailsDTO.getRoles().size());
        assertEquals("ROLE_USER", userDetailsDTO.getRoles().get(0));

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void registerNewUser_duplicateEmail_throwsException() {
        UserRegistrationDTO registrationDTO = new UserRegistrationDTO();
        registrationDTO.setEmail("test@example.com"); // Existing email

        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(DataIntegrityViolationException.class, () -> userManagementService.registerNewUser(registrationDTO));
    }

    @Test
    void findByEmail_success() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
        User foundUser = userManagementService.findByEmail("test@example.com");
        assertNotNull(foundUser);
        assertEquals("Test User", foundUser.getNome());
    }

    @Test
    void findByEmail_notFound_throwsException() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userManagementService.findByEmail("nonexistent@example.com"));
    }

    @Test
    void findDTOByEmail_success() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));

        UserDetailsDTO userDetailsDTO = userManagementService.findDTOByEmail("test@example.com");

        assertNotNull(userDetailsDTO);
        assertEquals("Test User", userDetailsDTO.getNome());
        assertEquals("test@example.com", userDetailsDTO.getEmail());
    }

    @Test
    void findDTOById_success() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testUser));

        UserDetailsDTO userDetailsDTO = userManagementService.findDTOById(1L);

        assertNotNull(userDetailsDTO);
        assertEquals("Test User", userDetailsDTO.getNome());
        assertEquals("test@example.com", userDetailsDTO.getEmail());
    }

    @Test
    void findAllUsers_success() {
        when(userRepository.findAll()).thenReturn(List.of(testUser));

        List<UserDetailsDTO> users = userManagementService.findAllUsers();

        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals("Test User", users.get(0).getNome());
    }

    @Test
    void updateUserByEmail_success() {
        UserUpdateDTO updateDTO = new UserUpdateDTO();
        updateDTO.setNome("Updated User");
        updateDTO.setEmail("updated@example.com");
        updateDTO.setTelefone("999-999-9999");
        updateDTO.setCpfCnpj("99999999999");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByCpfCnpj(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        UserDetailsDTO updatedUserDTO = userManagementService.updateUserByEmail("test@example.com", updateDTO);

        assertNotNull(updatedUserDTO);
        assertEquals("Updated User", updatedUserDTO.getNome());
        assertEquals("updated@example.com", updatedUserDTO.getEmail());
        assertEquals("999-999-9999", updatedUserDTO.getTelefone());
        assertEquals("99999999999", updatedUserDTO.getCpfCnpj());
    }

    @Test
    void updateUser_success() {
        UserUpdateDTO updateDTO = new UserUpdateDTO();
        updateDTO.setNome("Updated User");
        updateDTO.setEmail("updated@example.com");
        updateDTO.setTelefone("999-999-9999");
        updateDTO.setCpfCnpj("99999999999");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testUser));
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByCpfCnpj(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        UserDetailsDTO updatedUserDTO = userManagementService.updateUser(1L, updateDTO);

        assertNotNull(updatedUserDTO);
        assertEquals("Updated User", updatedUserDTO.getNome());
        assertEquals("updated@example.com", updatedUserDTO.getEmail());
        assertEquals("999-999-9999", updatedUserDTO.getTelefone());
        assertEquals("99999999999", updatedUserDTO.getCpfCnpj());
    }

    @Test
    void changePassword_success() {
        PasswordChangeDTO passwordChangeDTO = new PasswordChangeDTO();
        passwordChangeDTO.setOldPassword("oldPassword");
        passwordChangeDTO.setNewPassword("newPassword");
        passwordChangeDTO.setConfirmNewPassword("newPassword");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedNewPassword");

        assertDoesNotThrow(() -> userManagementService.changePassword("test@example.com", passwordChangeDTO));

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void changePassword_incorrectOldPassword_throwsException() {
        PasswordChangeDTO passwordChangeDTO = new PasswordChangeDTO();
        passwordChangeDTO.setOldPassword("incorrectPassword");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> userManagementService.changePassword("test@example.com", passwordChangeDTO));
    }

    @Test
    void deleteUser_success() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testUser));

        assertDoesNotThrow(() -> userManagementService.deleteUser(1L));

        verify(userRepository, times(1)).delete(any(User.class));
    }

    @Test
    void deleteUser_notFound_throwsException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userManagementService.deleteUser(1L));
    }
}