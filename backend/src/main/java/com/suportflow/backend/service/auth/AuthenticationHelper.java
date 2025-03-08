// src/main/java/com/suportflow/backend/service/auth/AuthenticationHelper.java
package com.suportflow.backend.service.auth;

import com.suportflow.backend.model.Cliente;
import com.suportflow.backend.model.User;
import com.suportflow.backend.repository.ClienteRepository;
import com.suportflow.backend.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component // Add this annotation to make it a Spring-managed bean
public class AuthenticationHelper {

    private final UserRepository userRepository;
    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationHelper(UserRepository userRepository,
                                ClienteRepository clienteRepository,
                                PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.clienteRepository = clienteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDetails authenticateUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));

        System.out.println("Attempting to authenticate user: " + email);
        System.out.println("Password matches: " + passwordEncoder.matches(password, user.getSenha()));

        // Check encoded password - use matches method from PasswordEncoder
        if (!passwordEncoder.matches(password, user.getSenha())) {
            throw new BadCredentialsException("Senha inválida");
        }
        return user;
    }

    public UserDetails authenticateCliente(String email, String password) {
        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente não encontrado: " + email));

        System.out.println("Attempting to authenticate client: " + email);
        System.out.println("Password matches: " + passwordEncoder.matches(password, cliente.getSenha()));
        // Check encoded password - cliente uses getSenha instead of getCpfCnpj for comparison
        if (!passwordEncoder.matches(password, cliente.getSenha())) {
            throw new BadCredentialsException("Credenciais inválidas");
        }
        return new org.springframework.security.core.userdetails.User(
                cliente.getEmail(),
                cliente.getSenha(),
                cliente.isAtivo(),
                true, true, true,
                java.util.Collections.emptyList()
        );
    }
}