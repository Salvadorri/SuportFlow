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
@Tag(name = "Users", description = "Operations related to user management") // Tag para agrupar no Swagger
public class UserController {

  @Autowired private UserManagementService userManagementService;

  @PostMapping
  @Operation(summary = "Register a new user", description = "Creates a new user account.")
  @ApiResponses(
          value = {
                  @ApiResponse(
                          responseCode = "201",
                          description = "User created successfully",
                          content = @Content(schema = @Schema(implementation = UserDetailsDTO.class))),
                  @ApiResponse(
                          responseCode = "400",
                          description = "Bad Request - Invalid input or validation error",
                          content = @Content), // No specific schema for error
                  @ApiResponse(
                          responseCode = "409",
                          description = "Conflict - User already exists (email or CPF/CNPJ)",
                          content = @Content),
                  @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
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
  @Operation(summary = "Get current user", description = "Retrieves the currently logged-in user's details.")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Successfully retrieved user details", content = @Content(schema = @Schema(implementation = UserDetailsDTO.class))),
          @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
          @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
  })
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<UserDetailsDTO> getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String email = authentication.getName();
    UserDetailsDTO userDetails = userManagementService.findDTOByEmail(email);
    return ResponseEntity.ok(userDetails);
  }


  @PutMapping("/me")
  @Operation(summary = "Update current user", description = "Updates the currently logged-in user's details.")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "User updated successfully", content = @Content(schema = @Schema(implementation = UserDetailsDTO.class))),
          @ApiResponse(responseCode = "400", description = "Bad Request - Invalid input", content = @Content),
          @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
          @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
          @ApiResponse(responseCode = "409", description = "Conflict - Email or CPF/CNPJ already exists", content = @Content), // Add 409 response
          @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
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
  @Operation(summary = "Change password", description = "Changes the currently logged-in user's password.")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Password changed successfully", content = @Content),
          @ApiResponse(responseCode = "400", description = "Bad Request - Invalid input or password mismatch", content = @Content),
          @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
          @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
          @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
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
  @Operation(summary = "Get user by ID", description = "Retrieves a user's details by their ID.")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Successfully retrieved user", content = @Content(schema = @Schema(implementation = UserDetailsDTO.class))),
          @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
          @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
          @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
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
  @Operation(summary = "Get all users", description = "Retrieves a list of all users.")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Successfully retrieved users", content = @Content(schema = @Schema(implementation = UserDetailsDTO.class))),
          @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
          @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
  })
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<List<UserDetailsDTO>> getAllUsers() {
    List<UserDetailsDTO> users = userManagementService.findAllUsers();
    return ResponseEntity.ok(users);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete user", description = "Deletes a user by their ID.")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "204", description = "User deleted successfully", content = @Content),
          @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
          @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
          @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
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
  @Operation(summary = "Update user by ID", description = "Updates a user's details by their ID.")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "User updated successfully", content = @Content(schema = @Schema(implementation = UserDetailsDTO.class))),
          @ApiResponse(responseCode = "400", description = "Bad Request - Invalid input", content = @Content),
          @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
          @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
          @ApiResponse(responseCode = "409", description = "Conflict - Email or CPF/CNPJ already exists", content = @Content),
          @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
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