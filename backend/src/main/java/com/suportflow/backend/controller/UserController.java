// src/main/java/com/suportflow/backend/controller/UserController.java
package com.suportflow.backend.controller;

import com.suportflow.backend.dto.PasswordChangeDTO;
import com.suportflow.backend.dto.UserDetailsDTO;
import com.suportflow.backend.dto.UserRegistrationDTO;
import com.suportflow.backend.dto.UserUpdateDTO;
import com.suportflow.backend.exception.UserNotFoundException;
import com.suportflow.backend.service.user.UserManagementService;
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
public class UserController {

  @Autowired private UserManagementService userManagementService;

  @PostMapping
  @PreAuthorize("isAuthenticated()") // Idealmente, o registro não deveria precisar de autenticação.  Considere remover.
  public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDTO registrationDTO) {
    try {
      UserDetailsDTO registeredUser = userManagementService.registerNewUser(registrationDTO);
      return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    } catch (Exception e) {
      // GlobalExceptionHandler will handle specific exceptions (like
      // UniqueFieldAlreadyExistsException).
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao registrar usuário: " + e.getMessage()); //Melhora na mensagem de erro.
    }
  }

  @GetMapping("/me")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<UserDetailsDTO> getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String email = authentication.getName(); // Get the email (username)
    UserDetailsDTO userDetails = userManagementService.findDTOByEmail(email);
    return ResponseEntity.ok(userDetails);
  }

  @PutMapping("/me")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<?> updateCurrentUser(
      @Valid @RequestBody UserUpdateDTO updateDTO) { // Use UserUpdateDTO
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String email = authentication.getName(); // Get the email

    try {
      UserDetailsDTO updatedUser = userManagementService.updateUserByEmail(email, updateDTO);
      return ResponseEntity.ok(updatedUser);
    } catch (UserNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao atualizar usuário.");
    }
  }

  @PatchMapping("/me/password")
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
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<List<UserDetailsDTO>> getAllUsers() {
    List<UserDetailsDTO> users = userManagementService.findAllUsers();
    return ResponseEntity.ok(users);
  }

  @DeleteMapping("/{id}")
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
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<?> updateUser(
      @PathVariable Long id, @Valid @RequestBody UserUpdateDTO updateDTO) { // Use UserUpdateDTO
    try {
      UserDetailsDTO updatedUser = userManagementService.updateUser(id, updateDTO);
      return ResponseEntity.ok(updatedUser);
    } catch (UserNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao atualizar usuário: "+e.getMessage());
    }
  }
}