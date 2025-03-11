// src/main/java/com/suportflow/backend/controller/ChamadoController.java
package com.suportflow.backend.controller;

import com.suportflow.backend.dto.*;
import com.suportflow.backend.service.chamado.ChamadoService;
import com.suportflow.backend.service.chamado.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Chamados", description = "Endpoints para gerenciar tickets de suporte (Chamados)")
public class ChamadoController {

    @Autowired
    private ChamadoService chamadoService;

    @Autowired
    private FileStorageService fileStorageService; // Inject FileStorageService

    // --- Chamado Endpoints ---

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Criar um novo ticket de suporte (Chamado)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Chamado criado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChamadoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Entrada inválida"),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária")
    })
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
    @Operation(summary = "Obter um ticket de suporte (Chamado) por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Chamado encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChamadoDTO.class))),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária"),
            @ApiResponse(responseCode = "404", description = "Chamado não encontrado")
    })
    public ResponseEntity<?> getChamadoById(@Parameter(description = "ID do Chamado a ser recuperado", example = "1") @PathVariable Long id) {
        try {
            ChamadoDTO chamadoDTO = chamadoService.getChamadoById(id);
            return ResponseEntity.ok(chamadoDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Obter todos os tickets de suporte (Chamados) com paginação e pesquisa opcional")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Chamados recuperados com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária")
    })
    public ResponseEntity<Page<ChamadoDTO>> getAllChamados(
            @Parameter(hidden = true) @PageableDefault(size = 10, sort = {"dataAbertura"}) Pageable pageable,
            @Parameter(description = "Termo de pesquisa para filtrar Chamados por título ou descrição", example = "Exemplo de Pesquisa") @RequestParam(required = false) String search) {

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
    @Operation(summary = "Obter todos os tickets de suporte (Chamados) para o cliente autenticado, com paginação e pesquisa opcional")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Chamados recuperados com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária")
    })
    public ResponseEntity<Page<ChamadoDTO>> getAllChamadosByCliente(
            @Parameter(hidden = true) @PageableDefault(size = 10, sort = {"dataAbertura"}) Pageable pageable,
            @Parameter(description = "Termo de pesquisa para filtrar Chamados por título ou descrição", example = "Exemplo de Pesquisa") @RequestParam(required = false) String search) {
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
    @Operation(summary = "Atualizar um ticket de suporte existente (Chamado)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Chamado atualizado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChamadoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Entrada inválida"),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária"),
            @ApiResponse(responseCode = "404", description = "Chamado não encontrado")
    })
    public ResponseEntity<?> updateChamado(@Parameter(description = "ID do Chamado a ser atualizado", example = "1") @PathVariable Long id, @Valid @RequestBody ChamadoUpdateDTO updateDTO) {
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
    @Operation(summary = "Excluir um ticket de suporte (Chamado)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Chamado excluído com sucesso"),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária"),
            @ApiResponse(responseCode = "404", description = "Chamado não encontrado")
    })
    public ResponseEntity<?> deleteChamado(@Parameter(description = "ID do Chamado a ser excluído", example = "1") @PathVariable Long id) {
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
    @Operation(summary = "Criar uma nova mensagem de chat para um ticket de suporte")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Mensagem criada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChatMensagemDTO.class))),
            @ApiResponse(responseCode = "400", description = "Entrada inválida"),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária"),
            @ApiResponse(responseCode = "404", description = "Chamado não encontrado")
    })
    public ResponseEntity<?> createMensagem(
            @Parameter(description = "ID do Chamado", example = "1") @RequestParam("chamadoId") Long chamadoId,
            @Parameter(description = "ID do Usuário (opcional, se enviando como usuário)", example = "1") @RequestParam(value = "usuarioId", required = false) Long usuarioId,
            @Parameter(description = "ID do Cliente (opcional, se enviando como cliente)", example = "1") @RequestParam(value = "clienteId", required = false) Long clienteId,
            @Parameter(description = "Conteúdo da mensagem", example = "Esta é uma mensagem de teste") @RequestParam("mensagem") String mensagem,
            @Parameter(description = "Arquivo para upload (opcional)") @RequestParam(value = "file", required = false) MultipartFile file) {

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
    @Operation(summary = "Obter todas as mensagens de chat para um ticket de suporte (Chamado) com paginação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mensagens recuperadas com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária"),
            @ApiResponse(responseCode = "404", description = "Chamado não encontrado")
    })
    public ResponseEntity<Page<ChatMensagemDTO>> getMensagensByChamado(
            @Parameter(description = "ID do Chamado", example = "1") @PathVariable Long chamadoId,
            @Parameter(hidden = true) @PageableDefault(size = 10, sort = {"dataEnvio"}) Pageable pageable) {
        try{
            Page<ChatMensagemDTO> mensagens = chamadoService.getMensagensByChamado(chamadoId, pageable);
            return ResponseEntity.ok(mensagens);
        }catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Page.empty());
        }

    }

    @GetMapping("/downloadFile/{fileName:.+}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Baixar um arquivo anexado a uma mensagem de chat")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Arquivo baixado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária"),
            @ApiResponse(responseCode = "404", description = "Arquivo não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Resource> downloadFile(@Parameter(description = "Nome do arquivo para baixar", example = "example.txt") @PathVariable String fileName) {
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