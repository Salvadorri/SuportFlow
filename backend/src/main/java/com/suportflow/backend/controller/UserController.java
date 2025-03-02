package com.suportflow.backend.controller;

import com.suportflow.backend.dto.UserDetailsDTO;
import com.suportflow.backend.dto.UserRegistrationDTO;
import com.suportflow.backend.service.user.UserManagementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users") // Prefixo /api/users
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

}