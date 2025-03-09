// src/main/java/com/suportflow/backend/exception/GlobalExceptionHandler.java
package com.suportflow.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
        Map<String, Object> body = createBody(HttpStatus.UNAUTHORIZED, "Credenciais inválidas", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
        Map<String, Object> body = createBody(HttpStatus.NOT_FOUND, "Usuário não encontrado", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UniqueFieldAlreadyExistsException.class)
    public ResponseEntity<Object> handleUniqueFieldAlreadyExistsException(UniqueFieldAlreadyExistsException ex, WebRequest request) {
        Map<String, Object> body = createBody(HttpStatus.CONFLICT, "Campo único já cadastrado", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    //Generic DataIntegrityViolationException handler
    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(org.springframework.dao.DataIntegrityViolationException ex, WebRequest request) {
          Map<String, Object> body = createBody(HttpStatus.CONFLICT, "Erro de integridade de dados", "Um campo único já está sendo usado.");
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        // Log the exception for debugging purposes (important!)
        System.err.println("Unhandled exception: " + ex.getMessage());
        ex.printStackTrace(); // Print the stack trace to the console

        Map<String, Object> body = createBody(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor", "Ocorreu um erro inesperado."); // Generic message
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Helper method to create the response body
    private Map<String, Object> createBody(HttpStatus status, String message, String details) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value()); // Include the HTTP status code
        body.put("error", status.getReasonPhrase()); // Include the HTTP status reason phrase
        body.put("message", message);
        body.put("details", details);
        return body;
    }
}