package com.suportflow.backend.service.auth;

import com.suportflow.backend.model.CustomUserDetails;
import com.suportflow.backend.model.User;
import com.suportflow.backend.service.auth.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Busca o usuário pelo email usando o UserService
        User user = userService.findByEmail(email);

        // Se o usuário não for encontrado, lança a exceção UsernameNotFoundException
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        // Retorna uma nova instância de CustomUserDetails com o objeto User encontrado
        return new CustomUserDetails(user);
    }
}