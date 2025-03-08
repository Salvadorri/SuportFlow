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

@Component
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

        // Check encoded password - use matches method from PasswordEncoder
        if (!passwordEncoder.matches(password, user.getSenha())) {
            throw new BadCredentialsException("Senha inválida");
        }
        return user; // Return the User object itself (it implements UserDetails)
    }

    public UserDetails authenticateCliente(String email, String password) {
        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente não encontrado: " + email));

        // Check encoded password
        if (!passwordEncoder.matches(password, cliente.getSenha())) {
            throw new BadCredentialsException("Senha inválida para o cliente");
        }
        // Create a UserDetails object for the client.  This is crucial!
        return new org.springframework.security.core.userdetails.User(
                cliente.getEmail(),
                cliente.getSenha(),
                cliente.isAtivo(), // Use the isAtivo() method
                true, true, true,
                java.util.Collections.emptyList() // No specific authorities for now
        );
    }
}