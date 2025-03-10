package com.suportflow.backend.controller;

import com.suportflow.backend.dto.ClienteDTO;
import com.suportflow.backend.dto.ClienteRegistrationDTO;
import com.suportflow.backend.dto.ClienteUpdateDTO;
import com.suportflow.backend.dto.PasswordChangeDTO;
import com.suportflow.backend.exception.UserNotFoundException;
import com.suportflow.backend.service.cliente.ClienteService;
import jakarta.validation.Valid;
import java.util.List;
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

  @PostMapping
  public ResponseEntity<?> registerCliente(@Valid @RequestBody ClienteRegistrationDTO registrationDTO) {
    try {
      ClienteDTO novoCliente = clienteService.save(registrationDTO);
      return new ResponseEntity<>(novoCliente, HttpStatus.CREATED);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao registrar cliente: " + e.getMessage());
    }
  }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()") // Only a logged-in client can access their own info.
    public ResponseEntity<ClienteDTO> getCurrentCliente() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Get the email (username) from the token.
        ClienteDTO clienteDTO = clienteService.findDTOByEmail(email);
        return ResponseEntity.ok(clienteDTO);
    }

    @PutMapping("/me")
    @PreAuthorize("isAuthenticated()")
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

    @PatchMapping("/me/password")
    @PreAuthorize("isAuthenticated()")
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

  @GetMapping
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<List<ClienteDTO>> getAllClientes() {
    List<ClienteDTO> clienteDTOs = clienteService.findAllDTO();
    return new ResponseEntity<>(clienteDTOs, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<ClienteDTO> getClienteById(@PathVariable Long id) {
    try {
      ClienteDTO clienteDTO = clienteService.findDTOById(id);
      return new ResponseEntity<>(clienteDTO, HttpStatus.OK);
    } catch (UserNotFoundException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PutMapping("/{id}")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<?> updateCliente(@PathVariable Long id, @Valid @RequestBody ClienteUpdateDTO clienteAtualizado) {
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
  public ResponseEntity<Void> deleteCliente(@PathVariable Long id) {
    try {
      clienteService.delete(id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (UserNotFoundException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }
}