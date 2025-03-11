// src/main/java/com/suportflow/backend/security/JwtAuthenticationFilter.java
package com.suportflow.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException; //Import
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final UserDetailsService clienteDetailsService; // Changed type here

    @Autowired
    public JwtAuthenticationFilter(JwtUtil jwtUtil,
                                   @Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService,
                                   @Qualifier("clienteDetailsService") UserDetailsService clienteDetailsService) {  //Added qualifier
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
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
            try {  //ADDED TRY CATCH
                username = jwtUtil.extractUsername(jwt);
            } catch (Exception e) {
                System.err.println("JWT Extraction Error: " + e.getMessage());
                e.printStackTrace();
                throw new BadCredentialsException("Erro ao extrair informações do JWT: " + e.getMessage(), e); // Propagate as BadCredentialsException
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = null;

            try {
                userDetails = this.userDetailsService.loadUserByUsername(username);
            } catch (Exception ignored) {
                // Log the exception!  Important for debugging.
                System.err.println("UserDetailsService exception: " + ignored.getMessage());
            }
            if (userDetails == null) { //adicionado para caso user details seja null
                try {
                    userDetails = this.clienteDetailsService.loadUserByUsername(username);
                } catch (Exception e) {
                    // Log the exception!  Important for debugging.
                    System.err.println("ClienteDetailsService exception: " + e.getMessage());
                    // Nao faz nada se nao encontrar.  This is OK, but log it.
                }
            }

            if (userDetails != null) {
                try {
                    if (jwtUtil.validateToken(jwt, userDetails)) {
                        // Extrair as roles/authorities do token JWT
                        List<String> roles = jwtUtil.extractRoles(jwt);
                        List<SimpleGrantedAuthority> authorities = roles.stream()
                                .map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toList());

                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, authorities);
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                } catch (Exception e) {
                    System.err.println("JWT Validation Error: " + e.getMessage());
                    e.printStackTrace();
                    throw new BadCredentialsException("Erro ao validar o JWT: " + e.getMessage(), e); // Propagate as BadCredentialsException
                }
            }
        }
        // SEMPRE chamar filterChain.doFilter, mesmo se o token for inválido.
        filterChain.doFilter(request, response);
    }
}