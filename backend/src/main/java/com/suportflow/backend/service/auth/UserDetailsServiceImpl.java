package com.suportflow.backend.service.auth;

import com.suportflow.backend.model.User;
import com.suportflow.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService; // Implementa a interface do Spring
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService { // Renomeado

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email) // Busca o usuário
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com email: " + email)); // Usa Optional

        return user; // Retorna o próprio User (que implementa UserDetails)
    }
}