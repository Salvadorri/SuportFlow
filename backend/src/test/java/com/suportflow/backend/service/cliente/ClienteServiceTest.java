// java/com/suportflow/backend/service/cliente/ClienteServiceTest.java
package com.suportflow.backend.service.cliente;

import com.suportflow.backend.dto.ClienteDTO;
import com.suportflow.backend.dto.ClienteRegistrationDTO;
import com.suportflow.backend.dto.ClienteUpdateDTO;
import com.suportflow.backend.dto.PasswordChangeDTO;
import com.suportflow.backend.exception.UniqueFieldAlreadyExistsException;
import com.suportflow.backend.exception.UserNotFoundException;
import com.suportflow.backend.model.Cliente;
import com.suportflow.backend.model.Empresa;
import com.suportflow.backend.repository.ClienteRepository;
import com.suportflow.backend.repository.EmpresaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private EmpresaRepository empresaRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente cliente;
    private Empresa empresa;

    @BeforeEach
    void setUp() {
        empresa = new Empresa();
        empresa.setId(1L);
        empresa.setNome("Empresa Teste");

        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Cliente Teste");
        cliente.setEmail("cliente@teste.com");
        cliente.setTelefone("12345678901");
        cliente.setCpfCnpj("12345678900");
        cliente.setSenha("senha123");
        cliente.setEmpresa(empresa);
        cliente.setDataCadastro(LocalDateTime.now());
        cliente.setAtivo(true);
    }

    @Test
    void findDTOById_ExistingId_ReturnsClienteDTO() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        ClienteDTO clienteDTO = clienteService.findDTOById(1L);
        assertNotNull(clienteDTO);
        assertEquals(cliente.getNome(), clienteDTO.getNome());
    }

    @Test
    void findDTOById_NonExistingId_ThrowsUserNotFoundException() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> clienteService.findDTOById(1L));
    }

    @Test
    void findDTOByEmail_ExistingEmail_ReturnsClienteDTO() {
        when(clienteRepository.findByEmail("cliente@teste.com")).thenReturn(Optional.of(cliente));
        ClienteDTO clienteDTO = clienteService.findDTOByEmail("cliente@teste.com");
        assertNotNull(clienteDTO);
        assertEquals(cliente.getNome(), clienteDTO.getNome());
    }

    @Test
    void findDTOByEmail_NonExistingEmail_ThrowsUserNotFoundException() {
        when(clienteRepository.findByEmail("cliente@teste.com")).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> clienteService.findDTOByEmail("cliente@teste.com"));
    }

    @Test
    void findEntityById_ExistingId_ReturnsCliente() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        Cliente foundCliente = clienteService.findEntityById(1L);
        assertNotNull(foundCliente);
        assertEquals(cliente.getNome(), foundCliente.getNome());
    }

    @Test
    void findEntityById_NonExistingId_ThrowsUserNotFoundException() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> clienteService.findEntityById(1L));
    }

    @Test
    void findAllDTO_ReturnsListOfClienteDTO() {
        when(clienteRepository.findAll()).thenReturn(Arrays.asList(cliente));
        List<ClienteDTO> clienteDTOList = clienteService.findAllDTO();
        assertNotNull(clienteDTOList);
        assertEquals(1, clienteDTOList.size());
        assertEquals(cliente.getNome(), clienteDTOList.get(0).getNome());
    }

    @Test
    void findByEmail_ExistingEmail_ReturnsCliente() {
        when(clienteRepository.findByEmail("cliente@teste.com")).thenReturn(Optional.of(cliente));
        Cliente foundCliente = clienteService.findByEmail("cliente@teste.com");
        assertNotNull(foundCliente);
        assertEquals(cliente.getNome(), foundCliente.getNome());
    }

    @Test
    void findByEmail_NonExistingEmail_ThrowsUserNotFoundException() {
        when(clienteRepository.findByEmail("cliente@teste.com")).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> clienteService.findByEmail("cliente@teste.com"));
    }

    @Test
    void save_ValidClienteRegistrationDTO_ReturnsClienteDTO() {
        ClienteRegistrationDTO registrationDTO = new ClienteRegistrationDTO();
        registrationDTO.setNome("Novo Cliente");
        registrationDTO.setEmail("novo@cliente.com");
        registrationDTO.setTelefone("11999999999");
        registrationDTO.setCpfCnpj("11111111111");
        registrationDTO.setSenha("senha123");
        registrationDTO.setEmpresaNome("Empresa Teste");

        when(empresaRepository.findByNome("Empresa Teste")).thenReturn(Optional.of(empresa));
        when(clienteRepository.existsByEmail("novo@cliente.com")).thenReturn(false);
        when(clienteRepository.existsByCpfCnpj("11111111111")).thenReturn(false);
        when(clienteRepository.existsByTelefone("11999999999")).thenReturn(false);
        when(passwordEncoder.encode("senha123")).thenReturn("encodedPassword");
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(invocation -> {
            Cliente savedCliente = invocation.getArgument(0);
            savedCliente.setId(2L); // Simulate ID generation by the database
            return savedCliente;
        });

        ClienteDTO clienteDTO = clienteService.save(registrationDTO);
        assertNotNull(clienteDTO);
        assertEquals("Novo Cliente", clienteDTO.getNome());
        assertEquals(2L, clienteDTO.getId());
    }

    @Test
    void save_DuplicateEmail_ThrowsUniqueFieldAlreadyExistsException() {
        ClienteRegistrationDTO registrationDTO = new ClienteRegistrationDTO();
        registrationDTO.setEmail("cliente@teste.com");
        when(clienteRepository.existsByEmail("cliente@teste.com")).thenReturn(true);
        assertThrows(UniqueFieldAlreadyExistsException.class, () -> clienteService.save(registrationDTO));
    }

    @Test
    void save_DuplicateCpfCnpj_ThrowsUniqueFieldAlreadyExistsException() {
        ClienteRegistrationDTO registrationDTO = new ClienteRegistrationDTO();
        registrationDTO.setCpfCnpj("12345678900");
        when(clienteRepository.existsByCpfCnpj("12345678900")).thenReturn(true);
        assertThrows(UniqueFieldAlreadyExistsException.class, () -> clienteService.save(registrationDTO));
    }

    @Test
    void save_DuplicateTelefone_ThrowsUniqueFieldAlreadyExistsException() {
        ClienteRegistrationDTO registrationDTO = new ClienteRegistrationDTO();
        registrationDTO.setTelefone("12345678901");
        when(clienteRepository.existsByTelefone("12345678901")).thenReturn(true);
        assertThrows(UniqueFieldAlreadyExistsException.class, () -> clienteService.save(registrationDTO));
    }

    @Test
    void save_NonExistingEmpresa_ThrowsIllegalArgumentException() {
        ClienteRegistrationDTO registrationDTO = new ClienteRegistrationDTO();
        registrationDTO.setEmpresaNome("Empresa Inexistente");
        when(empresaRepository.findByNome("Empresa Inexistente")).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> clienteService.save(registrationDTO));
    }

    @Test
    void update_ValidClienteUpdateDTO_ReturnsClienteDTO() {
        ClienteUpdateDTO updateDTO = new ClienteUpdateDTO();
        updateDTO.setNome("Cliente Atualizado");
        updateDTO.setEmail("atualizado@cliente.com");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.existsByEmail("atualizado@cliente.com")).thenReturn(false);
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ClienteDTO clienteDTO = clienteService.update(1L, updateDTO);

        assertNotNull(clienteDTO);
        assertEquals("Cliente Atualizado", clienteDTO.getNome());
        assertEquals("atualizado@cliente.com", clienteDTO.getEmail());
    }

    @Test
    void update_NonExistingId_ThrowsUserNotFoundException() {
        ClienteUpdateDTO updateDTO = new ClienteUpdateDTO();
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> clienteService.update(1L, updateDTO));
    }

    @Test
    void update_DuplicateEmail_ThrowsUniqueFieldAlreadyExistsException() {
        ClienteUpdateDTO updateDTO = new ClienteUpdateDTO();
        updateDTO.setEmail("atualizado@cliente.com");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.existsByEmail("atualizado@cliente.com")).thenReturn(true);

        assertThrows(UniqueFieldAlreadyExistsException.class, () -> clienteService.update(1L, updateDTO));
    }

     @Test
    void updateByEmail_ValidClienteUpdateDTO_ReturnsClienteDTO() {
        ClienteUpdateDTO updateDTO = new ClienteUpdateDTO();
        updateDTO.setNome("Cliente Atualizado");
        updateDTO.setEmail("atualizado@cliente.com");

        when(clienteRepository.findByEmail("cliente@teste.com")).thenReturn(Optional.of(cliente));
        when(clienteRepository.existsByEmail("atualizado@cliente.com")).thenReturn(false);
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ClienteDTO clienteDTO = clienteService.updateByEmail("cliente@teste.com", updateDTO);

        assertNotNull(clienteDTO);
        assertEquals("Cliente Atualizado", clienteDTO.getNome());
        assertEquals("atualizado@cliente.com", clienteDTO.getEmail());
    }

    @Test
    void updateByEmail_NonExistingEmail_ThrowsUserNotFoundException() {
        ClienteUpdateDTO updateDTO = new ClienteUpdateDTO();
        when(clienteRepository.findByEmail("cliente@teste.com")).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> clienteService.updateByEmail("cliente@teste.com", updateDTO));
    }

    @Test
    void updateByEmail_DuplicateEmail_ThrowsUniqueFieldAlreadyExistsException() {
        ClienteUpdateDTO updateDTO = new ClienteUpdateDTO();
        updateDTO.setEmail("atualizado@cliente.com");

        when(clienteRepository.findByEmail("cliente@teste.com")).thenReturn(Optional.of(cliente));
        when(clienteRepository.existsByEmail("atualizado@cliente.com")).thenReturn(true);

        assertThrows(UniqueFieldAlreadyExistsException.class, () -> clienteService.updateByEmail("cliente@teste.com", updateDTO));
    }

    @Test
    void changePassword_ValidPasswordChangeDTO_ChangesPassword() {
        PasswordChangeDTO passwordChangeDTO = new PasswordChangeDTO("oldPassword", "newPassword", "newPassword");
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(passwordEncoder.matches("oldPassword", "senha123")).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");

        clienteService.changePassword(1L, passwordChangeDTO);

        verify(clienteRepository, times(1)).save(cliente);
        assertEquals("encodedNewPassword", cliente.getSenha());
    }

    @Test
    void changePassword_IncorrectOldPassword_ThrowsIllegalArgumentException() {
        PasswordChangeDTO passwordChangeDTO = new PasswordChangeDTO("wrongPassword", "newPassword", "newPassword");
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(passwordEncoder.matches("wrongPassword", "senha123")).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> clienteService.changePassword(1L, passwordChangeDTO));
    }

    @Test
    void changePassword_MismatchedNewPasswords_ThrowsIllegalArgumentException() {
        PasswordChangeDTO passwordChangeDTO = new PasswordChangeDTO("oldPassword", "newPassword", "differentPassword");
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(passwordEncoder.matches("oldPassword", "senha123")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> clienteService.changePassword(1L, passwordChangeDTO));
    }
    @Test
    void changePasswordByEmail_ValidPasswordChangeDTO_ChangesPassword() {
        PasswordChangeDTO passwordChangeDTO = new PasswordChangeDTO("oldPassword", "newPassword", "newPassword");
        when(clienteRepository.findByEmail("cliente@teste.com")).thenReturn(Optional.of(cliente));
        when(passwordEncoder.matches("oldPassword", "senha123")).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");

        clienteService.changePasswordByEmail("cliente@teste.com", passwordChangeDTO);

        verify(clienteRepository, times(1)).save(cliente);
        assertEquals("encodedNewPassword", cliente.getSenha());
    }

    @Test
    void changePasswordByEmail_IncorrectOldPassword_ThrowsIllegalArgumentException() {
        PasswordChangeDTO passwordChangeDTO = new PasswordChangeDTO("wrongPassword", "newPassword", "newPassword");
        when(clienteRepository.findByEmail("cliente@teste.com")).thenReturn(Optional.of(cliente));
        when(passwordEncoder.matches("wrongPassword", "senha123")).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> clienteService.changePasswordByEmail("cliente@teste.com", passwordChangeDTO));
    }

    @Test
    void changePasswordByEmail_MismatchedNewPasswords_ThrowsIllegalArgumentException() {
        PasswordChangeDTO passwordChangeDTO = new PasswordChangeDTO("oldPassword", "newPassword", "differentPassword");
        when(clienteRepository.findByEmail("cliente@teste.com")).thenReturn(Optional.of(cliente));
        when(passwordEncoder.matches("oldPassword", "senha123")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> clienteService.changePasswordByEmail("cliente@teste.com", passwordChangeDTO));
    }

    @Test
    void delete_ExistingId_DeletesCliente() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        clienteService.delete(1L);
        verify(clienteRepository, times(1)).delete(cliente);
    }

    @Test
    void delete_NonExistingId_ThrowsUserNotFoundException() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> clienteService.delete(1L));
    }
}