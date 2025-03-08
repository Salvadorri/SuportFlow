// src/main/java/com/suportflow/backend/controller/TestController.java
package com.suportflow.backend.controller;

import com.suportflow.backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/token-info")
    public ResponseEntity<?> getTokenInfo(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7); // Remove "Bearer "

            String username = jwtUtil.extractUsername(token);
            List<String> roles = jwtUtil.extractRoles(token);

            Map<String, Object> tokenInfo = new HashMap<>();
            tokenInfo.put("username", username);
            tokenInfo.put("roles", roles);

            return ResponseEntity.ok(tokenInfo);
        } else {
            return ResponseEntity.badRequest().body("Authorization header inválido.");
        }
    }
}