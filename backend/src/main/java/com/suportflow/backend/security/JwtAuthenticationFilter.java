// src/main/java/com/suportflow/backend/security/JwtAuthenticationFilter.java
package com.suportflow.backend.security;

import com.suportflow.backend.service.auth.ClienteDetailsService;
import com.suportflow.backend.service.auth.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Collection;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService; // Use a interface, não a implementação específica
    private final ClienteDetailsService clienteDetailsService;

    @Autowired
    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService, ClienteDetailsService clienteDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService; // Injete a implementação UserDetailsServiceImpl
        this.clienteDetailsService = clienteDetailsService;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            username = jwtUtil.extractUsername(jwt);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = null;

            // 1. Tentar carregar como usuário
            try {
                userDetails = this.userDetailsService.loadUserByUsername(username);
            } catch (UsernameNotFoundException ignored) {
                // 2. Se falhar, tentar carregar como cliente
                try {
                    userDetails = this.clienteDetailsService.loadUserByUsername(username);
                } catch (UsernameNotFoundException e) {
                    // Se ambos falharem, o usuário não existe.  O filtro continuará,
                    // e a autenticação falhará mais adiante (provavelmente 403 Forbidden).
                }
            }

            // 3. Se encontramos um UserDetails (usuário ou cliente), validar o token
            if (userDetails != null && jwtUtil.validateToken(jwt, userDetails)) {
                // Obter as permissões (authorities) diretamente do UserDetails.
                Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, authorities); // Usar as permissões do UserDetails

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response); // Continuar a cadeia de filtros
    }
}