// src/main/java/com/suportflow/backend/service/auth/ClienteDetailsService.java
package com.suportflow.backend.service.auth;

import com.suportflow.backend.model.Cliente;
import com.suportflow.backend.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;

@Service
public class ClienteDetailsService implements UserDetailsService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente não encontrado com email: " + email));

        return new org.springframework.security.core.userdetails.User(
                cliente.getEmail(),
                cliente.getSenha(),
                cliente.isAtivo(),
                true, true, true,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_CLIENTE")) // Assign the role here
        );
    }
}