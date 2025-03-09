// src/main/java/com/suportflow/backend/controller/TestController.java
package com.suportflow.backend.controller;

import com.suportflow.backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/token-info")
    public ResponseEntity<?> getTokenInfo(@RequestHeader("Authorization") String authorizationHeader) {

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Token JWT ausente ou inválido.");
        }

        String jwt = authorizationHeader.substring(7);

        try {
            String username = jwtUtil.extractUsername(jwt);
            Long entityId = jwtUtil.extractEntityId(jwt);
            Boolean isCliente = jwtUtil.extractIsCliente(jwt); // Get if it's a Cliente

            Map<String, Object> tokenInfo = new HashMap<>();
            tokenInfo.put("username", username);
            tokenInfo.put("entityId", entityId);
            tokenInfo.put("Cliente", isCliente); //Display Cliente
            List<String> roles = jwtUtil.extractRoles(jwt);

            // Adicione a verificação de null aqui!
            if (roles != null) {
                tokenInfo.put("roles", roles);
            }

            return ResponseEntity.ok(tokenInfo);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao decodificar o token: " + e.getMessage());
        }
    }
    @GetMapping("/current-user")
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.ok("Nenhum usuário autenticado.");
        }

        Object principal = authentication.getPrincipal();
        String username = (principal instanceof String) ? (String) principal : authentication.getName();


        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("username", username);
        userInfo.put("roles", roles);
        //Não tem como pegar o entity id.

        return ResponseEntity.ok(userInfo);
    }


    @GetMapping("/admin-only")
    public ResponseEntity<String> adminOnly() {
        return ResponseEntity.ok("Acesso permitido apenas para administradores!");
    }
    @GetMapping("/user-or-admin")
    public ResponseEntity<String> userOrAdmin() {
        return ResponseEntity.ok("Acesso permitido para usuários ou administradores!");
    }
}