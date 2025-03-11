// src/main/java/com/suportflow/backend/dto/RefreshTokenDTO.java
package com.suportflow.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class RefreshTokenDTO {

    @NotBlank(message = "Refresh token is required") // Mensagem de erro mais descritiva.
    private String refreshToken;

    // Construtores - Manter o construtor padrão é uma boa prática, mesmo se você não o use explicitamente.
    public RefreshTokenDTO() {}

    public RefreshTokenDTO(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    // Getters e Setters
    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}