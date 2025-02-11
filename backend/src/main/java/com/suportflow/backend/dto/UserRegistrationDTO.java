package com.suportflow.backend.dto;

import java.time.LocalDateTime;

public class UserRegistrationDTO {
    private String nome;
    private String email;
    private String password;
    private String empresaNome; // Nome da empresa para associar o usuário (opcional)

    // Construtores, Getters e Setters

    public UserRegistrationDTO() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

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

    public String getEmpresaNome() {
        return empresaNome;
    }

    public void setEmpresaNome(String empresaNome) {
        this.empresaNome = empresaNome;
    }
}