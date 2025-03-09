// src/main/java/com/suportflow/backend/dto/AuthenticationRequest.java (Já existe, sem alterações)
package com.suportflow.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class AuthenticationRequest {

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    private String password; // Agora usado para senha de User e cpfCnpj de Cliente

    public AuthenticationRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }


    // Construtores (já existem)

    // Getters e Setters (já existem)
     public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}