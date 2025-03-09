// src/main/java/com/suportflow/backend/controller/ClienteController.java
package com.suportflow.backend.controller;

import com.suportflow.backend.dto.ClienteDTO;
import com.suportflow.backend.dto.ClienteRegistrationDTO;
import com.suportflow.backend.dto.ClienteUpdateDTO;
import com.suportflow.backend.dto.PasswordChangeDTO;
import com.suportflow.backend.exception.UserNotFoundException;
import com.suportflow.backend.service.cliente.ClienteService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

  @Autowired private ClienteService clienteService;

  @GetMapping
  @PreAuthorize("hasRole('USER')") // Only users can get all clients
  public ResponseEntity<List<ClienteDTO>> getAllClientes() {
    List<ClienteDTO> clienteDTOs = clienteService.findAllDTO();
    return new ResponseEntity<>(clienteDTOs, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  @PreAuthorize(
      "hasRole('USER') or (hasRole('CLIENTE') and #id == @clienteService.findEntityById(#id).getEmail())")
  public ResponseEntity<ClienteDTO> getClienteById(@PathVariable Long id) {
    try {
      ClienteDTO clienteDTO = clienteService.findById(id);
      return new ResponseEntity<>(clienteDTO, HttpStatus.OK);
    } catch (UserNotFoundException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping("/register")
  public ResponseEntity<?> createCliente(
      @Valid @RequestBody ClienteRegistrationDTO clienteRegistrationDTO) {
    try {
      ClienteDTO novoCliente = clienteService.save(clienteRegistrationDTO);
      return new ResponseEntity<>(novoCliente, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }
  }

  @PutMapping("/{id}")
  @PreAuthorize(
      "hasRole('USER') or (hasRole('CLIENTE') and #id == @clienteService.findEntityById(#id).getEmail())")
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

  @PatchMapping("/{id}/password")
  @PreAuthorize(
      "hasRole('USER') or (hasRole('CLIENTE') and #id == @clienteService.findEntityById(#id).getEmail())") // Clients can change their own password
  public ResponseEntity<?> changePassword(
      @PathVariable Long id, @Valid @RequestBody PasswordChangeDTO passwordChangeDTO) {
    try {
      clienteService.changePassword(id, passwordChangeDTO);
      return ResponseEntity.ok().body("Senha alterada com sucesso.");
    } catch (UserNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (IllegalArgumentException e) {
      // Invalid old password, passwords don't match, etc.
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Erro ao alterar a senha.");
    }
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('USER')") // Only users can delete clients
  public ResponseEntity<Void> deleteCliente(@PathVariable Long id) {
    try {
      clienteService.delete(id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (UserNotFoundException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }
}