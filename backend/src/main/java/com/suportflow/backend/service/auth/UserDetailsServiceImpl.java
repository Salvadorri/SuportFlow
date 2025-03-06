// src/main/java/com/suportflow/backend/service/auth/UserDetailsServiceImpl.java
package com.suportflow.backend.service.auth;

import com.suportflow.backend.model.User;
import com.suportflow.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true) // Important for LAZY loading
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com email: " + email));

        // No need for explicit permission loading IF using FetchType.EAGER.
        // If using FetchType.LAZY, make sure you use a repository method that
        //  performs a JOIN FETCH or loads the permissions in a separate query.
        //  For example, you might have a custom repository method:
        //  userRepository.findByEmailWithPermissions(email);

        return user; // Return the User object, which implements UserDetails.
    }
}