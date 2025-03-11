// src/main/java/com/suportflow/backend/controller/UserController.java
package com.suportflow.backend.controller;

import com.suportflow.backend.dto.PasswordChangeDTO;
import com.suportflow.backend.dto.UserDetailsDTO;
import com.suportflow.backend.dto.UserRegistrationDTO;
import com.suportflow.backend.dto.UserUpdateDTO;
import com.suportflow.backend.exception.UserNotFoundException;
import com.suportflow.backend.service.user.UserManagementService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Usuários", description = "Operações relacionadas ao gerenciamento de usuários")
public class UserController {

  @Autowired private UserManagementService userManagementService;

  @PostMapping
  @Operation(summary = "Registrar um novo usuário", description = "Cria uma nova conta de usuário.")
  @ApiResponses(
          value = {
                  @ApiResponse(
                          responseCode = "201",
                          description = "Usuário criado com sucesso",
                          content = @Content(schema = @Schema(implementation = UserDetailsDTO.class))),
                  @ApiResponse(
                          responseCode = "400",
                          description = "Requisição Inválida - Entrada inválida ou erro de validação",
                          content = @Content),
                  @ApiResponse(
                          responseCode = "409",
                          description = "Conflito - Usuário já existe (email ou CPF/CNPJ)",
                          content = @Content),
                  @ApiResponse(responseCode = "500", description = "Erro Interno do Servidor", content = @Content)
          })
  @PreAuthorize("permitAll()") // Permitir registro sem autenticação!
  public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDTO registrationDTO) {
    try {
      UserDetailsDTO registeredUser = userManagementService.registerNewUser(registrationDTO);
      return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    } catch (Exception e) {
      // GlobalExceptionHandler will handle specific exceptions.
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); //Melhora na mensagem de erro.
    }
  }

  @GetMapping("/me")
  @Operation(summary = "Obter usuário atual", description = "Recupera os detalhes do usuário atualmente logado.")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Detalhes do usuário recuperados com sucesso", content = @Content(schema = @Schema(implementation = UserDetailsDTO.class))),
          @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
          @ApiResponse(responseCode = "500", description = "Erro Interno do Servidor", content = @Content)
  })
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<UserDetailsDTO> getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String email = authentication.getName();
    UserDetailsDTO userDetails = userManagementService.findDTOByEmail(email);
    return ResponseEntity.ok(userDetails);
  }


  @PutMapping("/me")
  @Operation(summary = "Atualizar usuário atual", description = "Atualiza os detalhes do usuário atualmente logado.")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso", content = @Content(schema = @Schema(implementation = UserDetailsDTO.class))),
          @ApiResponse(responseCode = "400", description = "Requisição Inválida - Entrada inválida", content = @Content),
          @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
          @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content),
          @ApiResponse(responseCode = "409", description = "Conflito - Email ou CPF/CNPJ já existe", content = @Content),
          @ApiResponse(responseCode = "500", description = "Erro Interno do Servidor", content = @Content)
  })
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<?> updateCurrentUser(@Valid @RequestBody UserUpdateDTO updateDTO) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String email = authentication.getName();

    try {
      UserDetailsDTO updatedUser = userManagementService.updateUserByEmail(email, updateDTO);
      return ResponseEntity.ok(updatedUser);
    } catch (UserNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  @PatchMapping("/me/password")
  @Operation(summary = "Alterar senha", description = "Altera a senha do usuário atualmente logado.")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Senha alterada com sucesso", content = @Content),
          @ApiResponse(responseCode = "400", description = "Requisição Inválida - Entrada inválida ou senha incorreta", content = @Content),
          @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
          @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content),
          @ApiResponse(responseCode = "500", description = "Erro Interno do Servidor", content = @Content)
  })
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<?> changePassword(@Valid @RequestBody PasswordChangeDTO passwordChangeDTO) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String email = authentication.getName();

    try {
      userManagementService.changePassword(email, passwordChangeDTO);
      return ResponseEntity.ok().body("Senha alterada com sucesso.");
    } catch (UserNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao alterar a senha.");
    }
  }

  @GetMapping("/{id}")
  @Operation(summary = "Obter usuário por ID", description = "Recupera os detalhes de um usuário pelo seu ID.")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Usuário recuperado com sucesso", content = @Content(schema = @Schema(implementation = UserDetailsDTO.class))),
          @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
          @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content),
          @ApiResponse(responseCode = "500", description = "Erro Interno do Servidor", content = @Content)
  })
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<UserDetailsDTO> getUserById(@PathVariable Long id) {
    try {
      UserDetailsDTO user = userManagementService.findDTOById(id);
      return ResponseEntity.ok(user);
    } catch (UserNotFoundException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping
  @Operation(summary = "Obter todos os usuários", description = "Recupera uma lista de todos os usuários.")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Usuários recuperados com sucesso", content = @Content(schema = @Schema(implementation = UserDetailsDTO.class))),
          @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
          @ApiResponse(responseCode = "500", description = "Erro Interno do Servidor", content = @Content)
  })
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<List<UserDetailsDTO>> getAllUsers() {
    List<UserDetailsDTO> users = userManagementService.findAllUsers();
    return ResponseEntity.ok(users);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Excluir usuário", description = "Exclui um usuário pelo seu ID.")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso", content = @Content),
          @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
          @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content),
          @ApiResponse(responseCode = "500", description = "Erro Interno do Servidor", content = @Content)
  })
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<?> deleteUser(@PathVariable Long id) {
    try {
      userManagementService.deleteUser(id);
      return ResponseEntity.noContent().build();
    } catch (UserNotFoundException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @PutMapping("/{id}")
  @Operation(summary = "Atualizar usuário por ID", description = "Atualiza os detalhes de um usuário pelo seu ID.")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso", content = @Content(schema = @Schema(implementation = UserDetailsDTO.class))),
          @ApiResponse(responseCode = "400", description = "Requisição Inválida - Entrada inválida", content = @Content),
          @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
          @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content),
          @ApiResponse(responseCode = "409", description = "Conflito - Email ou CPF/CNPJ já existe", content = @Content),
          @ApiResponse(responseCode = "500", description = "Erro Interno do Servidor", content = @Content)
  })
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<?> updateUser(
          @PathVariable Long id, @Valid @RequestBody UserUpdateDTO updateDTO) {
    try {
      UserDetailsDTO updatedUser = userManagementService.updateUser(id, updateDTO);
      return ResponseEntity.ok(updatedUser);
    } catch (UserNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }
}