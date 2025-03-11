package com.suportflow.backend.service.chamado;

import com.suportflow.backend.dto.*;
import com.suportflow.backend.exception.UserNotFoundException;
import com.suportflow.backend.model.Chamado;
import com.suportflow.backend.model.ChatMensagem;
import com.suportflow.backend.model.Cliente;
import com.suportflow.backend.model.User;
import com.suportflow.backend.repository.ChamadoRepository;
import com.suportflow.backend.repository.ChatMensagemRepository;
import com.suportflow.backend.repository.ClienteRepository;
import com.suportflow.backend.repository.UserRepository;
import com.suportflow.backend.service.chamado.FileStorageService;
import com.suportflow.backend.service.cliente.ClienteService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ChamadoService {

    @Autowired private ChamadoRepository chamadoRepository;
    @Autowired private ChatMensagemRepository chatMensagemRepository;
    @Autowired private ClienteRepository clienteRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ClienteService clienteService;
    @Autowired private FileStorageService fileStorageService;


    // --- Chamado Operations ---

    @Transactional
    public ChamadoDTO createChamado(ChamadoCreateDTO createDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Cliente não encontrado com o e-mail: " + email));

        Chamado chamado = new Chamado();
        chamado.setCliente(cliente);
        chamado.setTitulo(createDTO.getTitulo());
        chamado.setDescricao(createDTO.getDescricao());
        chamado.setCategoria(createDTO.getCategoria());
        chamado.setPrioridade(createDTO.getPrioridade());
        chamado.setStatus(createDTO.getStatus()); // Use the provided status or default
        chamado.setDataAbertura(LocalDateTime.now());
        // atendente can be set later

        chamado = chamadoRepository.save(chamado);
        return convertToDTO(chamado);
    }

    public ChamadoDTO getChamadoById(Long id) {
        Chamado chamado = chamadoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Chamado not found with id: " + id));
        return convertToDTO(chamado);
    }

    public Page<ChamadoDTO> getAllChamados(Pageable pageable) {
        Page<Chamado> chamados = chamadoRepository.findAll(pageable);
        return chamados.map(this::convertToDTO);
    }

    public Page<ChamadoDTO> getAllChamadosByCliente(Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Cliente não encontrado com o email: " + email));
        Page<Chamado> chamados = chamadoRepository.findByCliente(cliente, pageable);
        return chamados.map(this::convertToDTO);
    }

    @Transactional
    public ChamadoDTO updateChamado(Long id, ChamadoUpdateDTO updateDTO) {
        Chamado chamado = chamadoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Chamado not found with id: " + id));

        if (updateDTO.getTitulo() != null) {
            chamado.setTitulo(updateDTO.getTitulo());
        }
        if (updateDTO.getDescricao() != null) {
            chamado.setDescricao(updateDTO.getDescricao());
        }
        if (updateDTO.getCategoria() != null) {
            chamado.setCategoria(updateDTO.getCategoria());
        }
        if (updateDTO.getPrioridade() != null) {
            chamado.setPrioridade(updateDTO.getPrioridade());
        }
        if (updateDTO.getStatus() != null) {
            chamado.setStatus(updateDTO.getStatus());
            if (updateDTO.getStatus() == com.suportflow.backend.model.StatusChamado.FECHADO && chamado.getDataFechamento() == null) {
                chamado.setDataFechamento(LocalDateTime.now());
            }
        }
        if (updateDTO.getAtendenteId() != null) {
            User atendente = userRepository.findById(updateDTO.getAtendenteId())
                    .orElseThrow(() -> new UserNotFoundException("Atendente not found with id: " + updateDTO.getAtendenteId()));
            chamado.setAtendente(atendente);
        }

        chamado = chamadoRepository.save(chamado);
        return convertToDTO(chamado);
    }

    @Transactional
    public void deleteChamado(Long id) {
        if (!chamadoRepository.existsById(id)) {
            throw new EntityNotFoundException("Chamado not found with id: " + id);
        }
        chamadoRepository.deleteById(id);
    }


    // --- ChatMensagem Operations ---
    @Transactional
    public ChatMensagemDTO createMensagem(ChatMensagemCreateDTO createDTO) {
        Chamado chamado = chamadoRepository.findById(createDTO.getChamadoId())
                .orElseThrow(() -> new EntityNotFoundException("Chamado not found with id: " + createDTO.getChamadoId()));

        ChatMensagem mensagem = new ChatMensagem();
        mensagem.setChamado(chamado);
        mensagem.setMensagem(createDTO.getMensagem());
        mensagem.setDataEnvio(LocalDateTime.now());
        mensagem.setCaminhoArquivo(createDTO.getCaminhoArquivo()); // Set file path

        // Determine sender (User or Cliente)
        if (createDTO.getUsuarioId() != null) {
            User usuario = userRepository.findById(createDTO.getUsuarioId())
                    .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado com ID: " + createDTO.getUsuarioId()));
            mensagem.setUsuario(usuario);
        } else if (createDTO.getClienteId() != null) {
            Cliente cliente = clienteRepository.findById(createDTO.getClienteId())
                    .orElseThrow(() -> new UserNotFoundException("Cliente não encontrado com ID: " + createDTO.getClienteId()));
            mensagem.setCliente(cliente);
        } else {
            throw new IllegalArgumentException("É necessário fornecer o ID do usuário ou do cliente.");
        }

        mensagem = chatMensagemRepository.save(mensagem);
        return convertToChatDTO(mensagem);
    }

    public Page<ChatMensagemDTO> getMensagensByChamado(Long chamadoId, Pageable pageable) {
        Chamado chamado = chamadoRepository.findById(chamadoId)
                .orElseThrow(() -> new EntityNotFoundException("Chamado not found with id: " + chamadoId));

        Page<ChatMensagem> mensagens = chatMensagemRepository.findByChamado(chamado, pageable);
        return mensagens.map(this::convertToChatDTO);
    }


    // --- Conversion Methods ---
    private ChamadoDTO convertToDTO(Chamado chamado) {
        ChamadoDTO dto = new ChamadoDTO();
        dto.setId(chamado.getId());
        dto.setClienteId(chamado.getCliente().getId());
        dto.setClienteNome(chamado.getCliente().getNome());
        if (chamado.getAtendente() != null) {
            dto.setAtendenteId(chamado.getAtendente().getId());
            dto.setAtendenteNome(chamado.getAtendente().getNome());
        }
        dto.setTitulo(chamado.getTitulo());
        dto.setDescricao(chamado.getDescricao());
        dto.setCategoria(chamado.getCategoria());
        dto.setStatus(chamado.getStatus());
        dto.setPrioridade(chamado.getPrioridade());
        dto.setDataAbertura(chamado.getDataAbertura());
        dto.setDataFechamento(chamado.getDataFechamento());
        return dto;
    }

    private ChatMensagemDTO convertToChatDTO(ChatMensagem mensagem) {
        ChatMensagemDTO dto = new ChatMensagemDTO();
        dto.setId(mensagem.getId());
        dto.setChamadoId(mensagem.getChamado().getId());
        if (mensagem.getUsuario() != null) {
            dto.setUsuarioId(mensagem.getUsuario().getId());
            dto.setUsuarioNome(mensagem.getUsuario().getNome());
        }
        if (mensagem.getCliente() != null) {
            dto.setClienteId(mensagem.getCliente().getId());
            dto.setClienteNome(mensagem.getCliente().getNome());
        }
        dto.setMensagem(mensagem.getMensagem());
        dto.setDataEnvio(mensagem.getDataEnvio());
        dto.setCaminhoArquivo(mensagem.getCaminhoArquivo()); // Include file path
        return dto;
    }

    // --- Search ---

    public Page<ChamadoDTO> searchChamados(String termo, Pageable pageable) {
        Page<Chamado> chamados = chamadoRepository.findByTituloOrDescricaoContainingIgnoreCase(termo, pageable);
        return chamados.map(this::convertToDTO);
    }

    public Page<ChamadoDTO> searchChamadosByCliente(String termo, Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Cliente não encontrado com o email: " + email));

        Page<Chamado> chamados = chamadoRepository.findByClienteAndTituloOrDescricaoContainingIgnoreCase(cliente, termo, pageable);
        return chamados.map(this::convertToDTO);
    }
}