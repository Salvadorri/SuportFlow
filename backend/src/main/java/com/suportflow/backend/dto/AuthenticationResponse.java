// src/main/java/com/suportflow/backend/dto/AuthenticationResponse.java (Já existe, sem alterações)
package com.suportflow.backend.dto;

public class AuthenticationResponse {

    private final String jwt;
    private final String refreshToken;

    public AuthenticationResponse(String jwt, String refreshToken) {
        this.jwt = jwt;
        this.refreshToken = refreshToken;
    }

    public String getJwt() {
        return jwt;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}