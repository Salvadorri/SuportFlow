package com.suportflow.backend.controller;

import com.suportflow.backend.dto.AuthenticationRequest;
import com.suportflow.backend.dto.AuthenticationResponse;
import com.suportflow.backend.model.User;
import com.suportflow.backend.security.JwtUtil;
import com.suportflow.backend.service.auth.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {

        // 1. Autentica o usuário usando o AuthenticationManager
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect email or password", e);
        }

        // 2. Carrega os detalhes do usuário autenticado
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());

        // 3. Gera o token JWT
        final String jwt = jwtUtil.generateToken(userDetails);

        // 4. Retorna o token JWT na resposta
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        // 5. Registra o novo usuário
        User registeredUser = userService.registerNewUser(user);

        // 6. Retorna o usuário registrado na resposta (ou um status de sucesso)
        return ResponseEntity.ok(registeredUser);
    }

    // Você pode adicionar outros endpoints, como /refresh, /logout, etc.
}