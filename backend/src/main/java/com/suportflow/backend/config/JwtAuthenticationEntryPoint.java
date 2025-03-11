// src/main/java/com/suportflow/backend/security/JwtAuthenticationEntryPoint.java
package com.suportflow.backend.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        // Set the WWW-Authenticate header (optional, but good practice)
        response.addHeader("WWW-Authenticate", "Bearer realm=\"suportflow\"");

        // Customize the error response
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);  // 401
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(
                String.format("{\"error\": \"%s\", \"message\": \"%s\"}",
                        HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                        authException.getMessage())); // include AuthenticationException message.
    }
}