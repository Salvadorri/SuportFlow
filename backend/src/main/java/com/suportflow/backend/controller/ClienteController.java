// src/main/java/com/suportflow/backend/controller/ClienteController.java
package com.suportflow.backend.controller;

import com.suportflow.backend.dto.ClienteDTO;
import com.suportflow.backend.dto.ClienteRegistrationDTO;
import com.suportflow.backend.dto.ClienteUpdateDTO;
import com.suportflow.backend.dto.PasswordChangeDTO;
import com.suportflow.backend.exception.UserNotFoundException;
import com.suportflow.backend.service.cliente.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clientes")
@Tag(name = "Clientes", description = "Endpoints para gerenciamento de clientes")
public class ClienteController {

  @Autowired private ClienteService clienteService;

  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Registrar um novo cliente")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "Cliente registrado com sucesso",
            content = @Content(schema = @Schema(implementation = ClienteDTO.class))),
        @ApiResponse(
            responseCode = "400",
            description = "Erro ao registrar cliente",
            content = @Content(schema = @Schema(implementation = String.class)))
      })
  public ResponseEntity<?> registerCliente(@Valid @RequestBody ClienteRegistrationDTO registrationDTO) {
    try {
      ClienteDTO novoCliente = clienteService.save(registrationDTO);
      return new ResponseEntity<>(novoCliente, HttpStatus.CREATED);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao registrar cliente: " + e.getMessage());
    }
  }

    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()") // Only a logged-in client can access their own info.
    @Operation(summary = "Obter perfil do cliente atual")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil do cliente recuperado com sucesso",
                    content = @Content(schema = @Schema(implementation = ClienteDTO.class))),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado", content = @Content)
    })
    public ResponseEntity<ClienteDTO> getCurrentCliente() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Get the email (username) from the token.
        ClienteDTO clienteDTO = clienteService.findDTOByEmail(email);
        return ResponseEntity.ok(clienteDTO);
    }

    @PutMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Atualizar perfil do cliente atual")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil do cliente atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = ClienteDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requisição Inválida", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado", content = @Content)
    })
    public ResponseEntity<?> updateCurrentCliente(@Valid @RequestBody ClienteUpdateDTO clienteUpdateDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        try {
            ClienteDTO updatedCliente = clienteService.updateByEmail(email, clienteUpdateDTO);
            return ResponseEntity.ok(updatedCliente);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao atualizar cliente: " + e.getMessage());
        }
    }

    @PatchMapping(value = "/me/password", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Alterar senha do cliente atual")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Senha alterada com sucesso", content = @Content),
            @ApiResponse(responseCode = "400", description = "Requisição Inválida", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro Interno do Servidor", content = @Content)
    })
    public ResponseEntity<?> changeCurrentClientePassword(@Valid @RequestBody PasswordChangeDTO passwordChangeDTO) {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      String email = authentication.getName();

      try {
        clienteService.changePasswordByEmail(email, passwordChangeDTO);
        return ResponseEntity.ok().body("Senha alterada com sucesso.");
      } catch (UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
      } catch (IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
      } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao alterar a senha.");
      }
    }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("isAuthenticated()")
  @Operation(summary = "Obter todos os clientes")
  @ApiResponse(
      responseCode = "200",
      description = "Lista de clientes recuperada com sucesso",
      content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteDTO.class)))
  public ResponseEntity<List<ClienteDTO>> getAllClientes() {
    List<ClienteDTO> clienteDTOs = clienteService.findAllDTO();
    return new ResponseEntity<>(clienteDTOs, HttpStatus.OK);
  }

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("isAuthenticated()")
  @Operation(summary = "Obter um cliente por ID")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Cliente recuperado com sucesso",
            content = @Content(schema = @Schema(implementation = ClienteDTO.class))),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
      })
  public ResponseEntity<ClienteDTO> getClienteById(@PathVariable Long id) {
    try {
      ClienteDTO clienteDTO = clienteService.findDTOById(id);
      return new ResponseEntity<>(clienteDTO, HttpStatus.OK);
    } catch (UserNotFoundException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("isAuthenticated()")
  @Operation(summary = "Atualizar um cliente por ID")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Cliente atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = ClienteDTO.class))),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
        @ApiResponse(
            responseCode = "400",
            description = "Requisição Inválida",
            content = @Content(schema = @Schema(implementation = String.class)))
      })
  public ResponseEntity<?> updateCliente(
      @PathVariable Long id, @Valid @RequestBody ClienteUpdateDTO clienteAtualizado) {
    try {
      ClienteDTO clienteAtualizadoResult = clienteService.update(id, clienteAtualizado);
      return new ResponseEntity<>(clienteAtualizadoResult, HttpStatus.OK);
    } catch (UserNotFoundException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("isAuthenticated()")
  @Operation(summary = "Deletar um cliente por ID")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "Cliente deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
      })
  public ResponseEntity<Void> deleteCliente(@PathVariable Long id) {
    try {
      clienteService.delete(id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (UserNotFoundException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }
}