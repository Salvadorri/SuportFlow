package com.suportflow.backend.controller;

import com.suportflow.backend.dto.UserDetailsDTO;
import com.suportflow.backend.dto.UserRegistrationDTO;
import com.suportflow.backend.service.user.UserManagementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserManagementService userManagementService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDTO registrationDTO) {
        try {
            UserDetailsDTO registeredUser = userManagementService.registerNewUser(registrationDTO);
            return ResponseEntity.ok(registeredUser);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Já existe um usuário com este e-mail.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao registrar usuário.");
        }
    }

    @GetMapping("/me/permissions")
    public ResponseEntity<List<String>> getMyPermissions() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401 Unauthorized
        }

        List<String> permissions = userManagementService.getCurrentUserPermissions();
        return ResponseEntity.ok(permissions);
    }
}