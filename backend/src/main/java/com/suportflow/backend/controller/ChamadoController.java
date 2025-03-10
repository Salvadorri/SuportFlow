// src/main/java/com/suportflow/backend/controller/ChamadoController.java
package com.suportflow.backend.controller;

import com.suportflow.backend.dto.*;
import com.suportflow.backend.service.chamado.ChamadoService;
import com.suportflow.backend.service.chamado.FileStorageService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/chamados")
public class ChamadoController {

    @Autowired
    private ChamadoService chamadoService;

    @Autowired
    private FileStorageService fileStorageService; // Inject FileStorageService

    // --- Chamado Endpoints ---

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createChamado(@Valid @RequestBody ChamadoCreateDTO createDTO) {
        try {
            ChamadoDTO createdChamado = chamadoService.createChamado(createDTO);
            return new ResponseEntity<>(createdChamado, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao criar chamado: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getChamadoById(@PathVariable Long id) {
        try {
            ChamadoDTO chamadoDTO = chamadoService.getChamadoById(id);
            return ResponseEntity.ok(chamadoDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<ChamadoDTO>> getAllChamados(
            @PageableDefault(size = 10, sort = {"dataAbertura"}) Pageable pageable,
            @RequestParam(required = false) String search) {

        Page<ChamadoDTO> chamados;

        if (search != null && !search.trim().isEmpty()) {
            chamados = chamadoService.searchChamados(search, pageable);
        } else {
            chamados = chamadoService.getAllChamados(pageable);
        }
        return ResponseEntity.ok(chamados);
    }


    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<ChamadoDTO>> getAllChamadosByCliente(
            @PageableDefault(size = 10, sort = {"dataAbertura"}) Pageable pageable,
            @RequestParam(required = false) String search) {
        Page<ChamadoDTO> chamados;

        if (search != null && !search.trim().isEmpty()) {
            chamados = chamadoService.searchChamadosByCliente(search, pageable);
        } else {
            chamados = chamadoService.getAllChamadosByCliente(pageable);
        }
        return ResponseEntity.ok(chamados);
    }


    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateChamado(@PathVariable Long id, @Valid @RequestBody ChamadoUpdateDTO updateDTO) {
        try {
            ChamadoDTO updatedChamado = chamadoService.updateChamado(id, updateDTO);
            return ResponseEntity.ok(updatedChamado);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao atualizar chamado: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteChamado(@PathVariable Long id) {
        try {
            chamadoService.deleteChamado(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // --- ChatMensagem Endpoints ---

    @PostMapping("/mensagens")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createMensagem(
            @RequestParam("chamadoId") Long chamadoId,
            @RequestParam(value = "usuarioId", required = false) Long usuarioId,
            @RequestParam(value = "clienteId", required = false) Long clienteId,
            @RequestParam("mensagem") String mensagem,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        try {
            String filePath = null;
            if (file != null && !file.isEmpty()) {
                filePath = fileStorageService.storeFile(file);
            }

            ChatMensagemCreateDTO createDTO = new ChatMensagemCreateDTO();
            createDTO.setChamadoId(chamadoId);
            createDTO.setUsuarioId(usuarioId);
            createDTO.setClienteId(clienteId);
            createDTO.setMensagem(mensagem);
            createDTO.setCaminhoArquivo(filePath);

            ChatMensagemDTO createdMensagem = chamadoService.createMensagem(createDTO);
            return new ResponseEntity<>(createdMensagem, HttpStatus.CREATED);

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar mensagem: " + e.getMessage());
        }
    }
    @GetMapping("/{chamadoId}/mensagens")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<ChatMensagemDTO>> getMensagensByChamado(
            @PathVariable Long chamadoId,
            @PageableDefault(size = 10, sort = {"dataEnvio"}) Pageable pageable) {
        try{
            Page<ChatMensagemDTO> mensagens = chamadoService.getMensagensByChamado(chamadoId, pageable);
            return ResponseEntity.ok(mensagens);
        }catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Page.empty());
        }

    }

    @GetMapping("/downloadFile/{fileName:.+}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        try {
            Path filePath = fileStorageService.loadFile(fileName);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}