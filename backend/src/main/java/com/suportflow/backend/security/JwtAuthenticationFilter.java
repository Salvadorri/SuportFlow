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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final ClienteDetailsService clienteDetailsService; // Injetar o ClienteDetailsService

    @Autowired
    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService, ClienteDetailsService clienteDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.clienteDetailsService = clienteDetailsService; // Injetar no construtor
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            username = jwtUtil.extractUsername(jwt); // Usar extractUsername
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = null;
            // Tentar carregar como usuário
            try {
                userDetails = this.userDetailsService.loadUserByUsername(username);
            } catch (Exception e) {
                // Tratamento do erro
            }

            // Se não for um usuário, tentar carregar como cliente
            if (userDetails == null) {
                try {
                    userDetails = this.clienteDetailsService.loadUserByUsername(username);
                } catch (Exception e) {
                    // Tratamento do erro
                }
            }
            // Se userDetails ainda for nulo após tentar carregar o usuário e o cliente,
            // o token é inválido ou o usuário/cliente não existe mais.
            if (userDetails != null && jwtUtil.validateToken(jwt, userDetails)) {
                // Obter as permissões (authorities) diretamente do UserDetails.
                Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, authorities);
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}