package com.suportflow.backend.service.auth;

import com.suportflow.backend.exception.UserNotFoundException;
import com.suportflow.backend.model.CustomUserDetails;
import com.suportflow.backend.model.User;
import com.suportflow.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository; // Injete o UserRepository

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Busca o usuário pelo email usando o UserRepository diretamente
        User user = userRepository.findByEmail(email);

        // Se o usuário não for encontrado, lança a exceção UsernameNotFoundException
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        // Retorna uma nova instância de CustomUserDetails com o objeto User encontrado
        return new CustomUserDetails(user);
    }
}