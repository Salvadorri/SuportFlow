package com.suportflow.backend.model;

import com.suportflow.backend.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Se você tiver papéis/permissões (roles/authorities) associados aos seus usuários,
        // você precisaria buscar e retornar esses papéis aqui.
        // Por exemplo, se você tivesse uma entidade Role e um relacionamento
        // ManyToMany entre User e Role, você faria algo assim:
        // return user.getRoles().stream()
        //        .map(role -> new SimpleGrantedAuthority(role.getName()))
        //        .collect(Collectors.toList());

        // Neste exemplo, estamos retornando uma lista vazia, pois não estamos
        // usando papéis/permissões.
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail(); // Retorna o email do usuário, que estamos usando como username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Você pode adicionar lógica para verificar a expiração da conta, se necessário
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Você pode adicionar lógica para verificar se a conta está bloqueada, se necessário
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Você pode adicionar lógica para verificar se as credenciais estão expiradas, se necessário
    }

    @Override
    public boolean isEnabled() {
        return user.getAtivo(); // Verifica se o usuário está ativo (campo 'ativo' na entidade User)
    }
}