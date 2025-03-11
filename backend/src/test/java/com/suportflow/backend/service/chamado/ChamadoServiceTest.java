// src/test/java/com/suportflow/backend/service/chamado/ChamadoServiceTest.java
package com.suportflow.backend.service.chamado;

import com.suportflow.backend.dto.*;
import com.suportflow.backend.exception.UserNotFoundException;
import com.suportflow.backend.model.*;
import com.suportflow.backend.repository.ChamadoRepository;
import com.suportflow.backend.repository.ChatMensagemRepository;
import com.suportflow.backend.repository.ClienteRepository;
import com.suportflow.backend.repository.UserRepository;
import com.suportflow.backend.service.cliente.ClienteService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChamadoServiceTest {

    @Mock
    private ChamadoRepository chamadoRepository;
    @Mock
    private ChatMensagemRepository chatMensagemRepository;
    @Mock
    private ClienteRepository clienteRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ClienteService clienteService;  // Mocked, even if not directly used in every test
    @Mock
    private FileStorageService fileStorageService; // Mocked, even if not directly used in every test
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private ChamadoService chamadoService;

    private Cliente testCliente;
    private User testUser;
    private Chamado testChamado;
    private ChamadoCreateDTO testChamadoCreateDTO;
    private ChamadoUpdateDTO testChamadoUpdateDTO;
    private ChatMensagem testChatMensagem;
    private ChatMensagemCreateDTO testChatMensagemCreateDTO;


    @BeforeEach
    void setUp() {
        // Common setup for all tests

        // --- Authentication Mock Setup (Critical for secured methods) ---
        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        lenient().when(authentication.getName()).thenReturn("test@example.com");

        // --- Data Setup ---
        testCliente = new Cliente();
        testCliente.setId(1L);
        testCliente.setEmail("test@example.com");
        testCliente.setNome("Test Cliente");

        testUser = new User();
        testUser.setId(2L);
        testUser.setEmail("atendente@example.com");
        testUser.setNome("Test Atendente");

        testChamado = createTestChamado(testCliente, testUser);

        testChamadoCreateDTO = createChamadoCreateDTO();
        testChamadoUpdateDTO = createChamadoUpdateDTO();

        testChatMensagem = new ChatMensagem();
        testChatMensagem.setId(1L);
        testChatMensagem.setChamado(testChamado);
        testChatMensagem.setCliente(testCliente);
        testChatMensagem.setMensagem("Test Message");
        testChatMensagem.setDataEnvio(LocalDateTime.now());

        testChatMensagemCreateDTO = new ChatMensagemCreateDTO();
        testChatMensagemCreateDTO.setChamadoId(testChamado.getId());
        testChatMensagemCreateDTO.setClienteId(testCliente.getId());
        testChatMensagemCreateDTO.setMensagem("Test Message");
    }

    private Chamado createTestChamado(Cliente cliente, User atendente) {
        Chamado chamado = new Chamado();
        chamado.setId(1L);
        chamado.setCliente(cliente);
        chamado.setAtendente(atendente);
        chamado.setTitulo("Test Chamado");
        chamado.setDescricao("Test Description");
        chamado.setCategoria(CategoriaChamado.SUPORTE_TECNICO);
        chamado.setStatus(StatusChamado.ABERTO);
        chamado.setPrioridade(PrioridadeChamado.ALTA);
        chamado.setDataAbertura(LocalDateTime.now());
        return chamado;
    }

    private ChamadoCreateDTO createChamadoCreateDTO() {
        ChamadoCreateDTO dto = new ChamadoCreateDTO();
        dto.setTitulo("Test Chamado Create");
        dto.setDescricao("Test Description Create");
        dto.setCategoria(CategoriaChamado.SUPORTE_TECNICO);
        dto.setPrioridade(PrioridadeChamado.MEDIA);
        dto.setStatus(StatusChamado.ABERTO);
        return dto;
    }
    private ChamadoUpdateDTO createChamadoUpdateDTO() {
        ChamadoUpdateDTO dto = new ChamadoUpdateDTO();
        dto.setTitulo("Updated Chamado");
        dto.setStatus(StatusChamado.EM_ANDAMENTO);
        dto.setAtendenteId(testUser.getId());
        return dto;
    }


    // --- Chamado Tests ---

    @Test
    void createChamado_Success() {
        when(clienteRepository.findByEmail(anyString())).thenReturn(Optional.of(testCliente));
        when(chamadoRepository.save(any(Chamado.class))).thenReturn(testChamado);

        ChamadoDTO result = chamadoService.createChamado(testChamadoCreateDTO);

        assertNotNull(result);
        assertEquals(testChamado.getTitulo(), result.getTitulo());
        verify(chamadoRepository).save(any(Chamado.class));
    }

    @Test
    void createChamado_ClienteNotFound_ThrowsException() {
        when(clienteRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> chamadoService.createChamado(testChamadoCreateDTO));
    }

    @Test
    void getChamadoById_Success() {
        when(chamadoRepository.findById(testChamado.getId())).thenReturn(Optional.of(testChamado));

        ChamadoDTO result = chamadoService.getChamadoById(testChamado.getId());

        assertNotNull(result);
        assertEquals(testChamado.getTitulo(), result.getTitulo());
    }
    @Test
    void getChamadoById_NotFound_ThrowsException() {
        when(chamadoRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> chamadoService.getChamadoById(999L));
    }

    @Test
    void getAllChamados_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Chamado> chamadoPage = new PageImpl<>(List.of(testChamado), pageable, 1);
        when(chamadoRepository.findAll(pageable)).thenReturn(chamadoPage);

        Page<ChamadoDTO> result = chamadoService.getAllChamados(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void getAllChamadosByCliente_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Chamado> chamadoPage = new PageImpl<>(List.of(testChamado), pageable, 1);
        when(clienteRepository.findByEmail(anyString())).thenReturn(Optional.of(testCliente));
        when(chamadoRepository.findByCliente(testCliente, pageable)).thenReturn(chamadoPage);

        Page<ChamadoDTO> result = chamadoService.getAllChamadosByCliente(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }
    @Test
    void getAllChamadosByCliente_ClienteNotFound_ThrowsException() {
        Pageable pageable = PageRequest.of(0, 10);
        when(clienteRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> chamadoService.getAllChamadosByCliente(pageable));
    }

    @Test
    void updateChamado_Success() {
        when(chamadoRepository.findById(testChamado.getId())).thenReturn(Optional.of(testChamado));
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser)); //For Atendente Update
        when(chamadoRepository.save(any(Chamado.class))).thenReturn(testChamado);

        ChamadoDTO result = chamadoService.updateChamado(testChamado.getId(), testChamadoUpdateDTO);

        assertNotNull(result);
        assertEquals("Updated Chamado", result.getTitulo());  // Check updated title
        assertEquals(StatusChamado.EM_ANDAMENTO, result.getStatus()); //Check updated Status
        assertEquals(testUser.getId(), result.getAtendenteId()); //Check updated Atendente
        verify(chamadoRepository).save(any(Chamado.class));
    }

    @Test
    void updateChamado_SetStatusToFechado_SetsDataFechamento() {
        testChamado.setDataFechamento(null); // Ensure dataFechamento is initially null
        testChamadoUpdateDTO.setStatus(StatusChamado.FECHADO);
        testChamadoUpdateDTO.setAtendenteId(testUser.getId()); //Set Atendente Id

        when(chamadoRepository.findById(testChamado.getId())).thenReturn(Optional.of(testChamado));
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser)); // Mock the user repository
        when(chamadoRepository.save(any(Chamado.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Return the saved Chamado

        ChamadoDTO result = chamadoService.updateChamado(testChamado.getId(), testChamadoUpdateDTO);

        assertNotNull(result);
        assertNotNull(result.getDataFechamento());  // Verify dataFechamento is set
    }

    @Test
    void updateChamado_NotFound_ThrowsException() {
        when(chamadoRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> chamadoService.updateChamado(999L, testChamadoUpdateDTO));
    }
    @Test
    void updateChamado_AtendenteNotFound_ThrowsException() {
        when(chamadoRepository.findById(testChamado.getId())).thenReturn(Optional.of(testChamado));
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        testChamadoUpdateDTO.setAtendenteId(999L); // Set an invalid atendente ID

        assertThrows(UserNotFoundException.class, ()-> chamadoService.updateChamado(testChamado.getId(), testChamadoUpdateDTO));
    }

    @Test
    void deleteChamado_Success() {
        when(chamadoRepository.existsById(testChamado.getId())).thenReturn(true);
        doNothing().when(chamadoRepository).deleteById(testChamado.getId());

        chamadoService.deleteChamado(testChamado.getId());

        verify(chamadoRepository).deleteById(testChamado.getId());
    }

    @Test
    void deleteChamado_NotFound_ThrowsException() {
        when(chamadoRepository.existsById(anyLong())).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> chamadoService.deleteChamado(999L));
    }

    // --- ChatMensagem Tests ---

    @Test
    void createMensagem_Success() {
        when(chamadoRepository.findById(testChamado.getId())).thenReturn(Optional.of(testChamado));
        when(clienteRepository.findById(testCliente.getId())).thenReturn(Optional.of(testCliente));
        when(chatMensagemRepository.save(any(ChatMensagem.class))).thenReturn(testChatMensagem);

        ChatMensagemDTO result = chamadoService.createMensagem(testChatMensagemCreateDTO);

        assertNotNull(result);
        assertEquals("Test Message", result.getMensagem());
        verify(chatMensagemRepository).save(any(ChatMensagem.class));
    }

    @Test
    void createMensagem_ChamadoNotFound_ThrowsException() {
        when(chamadoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> chamadoService.createMensagem(testChatMensagemCreateDTO));
    }

    @Test
    void createMensagem_UserOrClienteNotFound_ThrowsException() {
        // Test for User Not Found
        testChatMensagemCreateDTO.setClienteId(null);
        testChatMensagemCreateDTO.setUsuarioId(999L); // Invalid User ID
        when(chamadoRepository.findById(testChamado.getId())).thenReturn(Optional.of(testChamado));
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> chamadoService.createMensagem(testChatMensagemCreateDTO));

        // Test for Client Not Found
        testChatMensagemCreateDTO.setUsuarioId(null);
        testChatMensagemCreateDTO.setClienteId(999L); // Invalid Client ID
        when(clienteRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> chamadoService.createMensagem(testChatMensagemCreateDTO));

        //Test neither User or Client provided
        testChatMensagemCreateDTO.setUsuarioId(null);
        testChatMensagemCreateDTO.setClienteId(null);
        assertThrows(IllegalArgumentException.class, () -> chamadoService.createMensagem(testChatMensagemCreateDTO));
    }

    @Test
    void getMensagensByChamado_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ChatMensagem> mensagemPage = new PageImpl<>(List.of(testChatMensagem), pageable, 1);

        when(chamadoRepository.findById(testChamado.getId())).thenReturn(Optional.of(testChamado));
        when(chatMensagemRepository.findByChamado(testChamado, pageable)).thenReturn(mensagemPage);


        Page<ChatMensagemDTO> result = chamadoService.getMensagensByChamado(testChamado.getId(), pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Test Message", result.getContent().get(0).getMensagem());

    }

    @Test
    void getMensagensByChamado_ChamadoNotFound() {
        Pageable pageable = PageRequest.of(0, 10);
        when(chamadoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> chamadoService.getMensagensByChamado(999L, pageable));

    }

    @Test
    void searchChamados_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Chamado> chamadoPage = new PageImpl<>(List.of(testChamado), pageable, 1);
        when(chamadoRepository.findByTituloOrDescricaoContainingIgnoreCase(anyString(), any(Pageable.class))).thenReturn(chamadoPage);

        Page<ChamadoDTO> chamados = chamadoService.searchChamados("test", pageable); // Changed variable name

        assertNotNull(chamados); // Changed variable name
        assertEquals(1, chamados.getTotalElements()); // Changed variable name
    }

    @Test
    void searchChamadosByCliente_ClienteNotFound_ThrowsException() {
        Pageable pageable = PageRequest.of(0, 10);
        when(clienteRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> chamadoService.searchChamadosByCliente("test", pageable));
    }
}