package com.suportflow.backend.config;

import com.suportflow.backend.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity; // Importante!
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // Habilita @PreAuthorize e @PostAuthorize
@Import(PasswordEncoderConfig.class)
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        // Autenticação e Registro
                        .requestMatchers("/api/auth/**").permitAll()  // Permite tudo em /api/auth (login, registro, etc.)

                        // Empresas
                        .requestMatchers(HttpMethod.GET, "/api/empresas").hasAuthority("LISTAR_EMPRESAS")
                        .requestMatchers(HttpMethod.POST, "/api/empresas").hasAuthority("CRIAR_EMPRESA")
                        .requestMatchers(HttpMethod.PUT, "/api/empresas/**").hasAuthority("EDITAR_EMPRESA")
                        .requestMatchers(HttpMethod.DELETE, "/api/empresas/**").hasAuthority("EXCLUIR_EMPRESA")

                        // Usuários (Administradores)
                        .requestMatchers(HttpMethod.POST, "/api/users").hasAuthority("CRIAR_USUARIO") //Já foi implementado
                        .requestMatchers(HttpMethod.GET, "/api/users").hasAuthority("LISTAR_USUARIOS")
                        .requestMatchers(HttpMethod.PUT, "/api/users/**").hasAuthority("ATUALIZAR_USUARIO")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasAuthority("EXCLUIR_USUARIO")

                        //Permissões
                        .requestMatchers(HttpMethod.GET, "/api/permissoes").hasAuthority("GERENCIAR_PERMISSOES")


                        // Chamados
                        .requestMatchers(HttpMethod.POST, "/api/chamados").hasAnyAuthority("CRIAR_CHAMADO", "ABRIR_CHAMADO_SUPORTE") //Funcionarios e Gerentes
                        .requestMatchers(HttpMethod.GET, "/api/chamados").hasAnyAuthority("LISTAR_CHAMADOS_PROPRIOS", "LISTAR_CHAMADOS_TODOS", "VISUALIZAR_CHAMADOS") //Funcionarios e Gerentes
                        .requestMatchers(HttpMethod.PUT, "/api/chamados/**").hasAnyAuthority("EDITAR_CHAMADO_PROPRIO", "EDITAR_CHAMADO_TODOS", "ATRIBUIR_CHAMADOS", "RESPONDER_CHAMADOS", "FECHAR_CHAMADOS") //Funcionarios e Gerentes
                        .requestMatchers(HttpMethod.DELETE, "/api/chamados/**").hasAnyAuthority("EXCLUIR_CHAMADO_PROPRIO", "EXCLUIR_CHAMADO_TODOS") //Funcionarios e Gerentes

                        // Chatbot
                        .requestMatchers(HttpMethod.PUT, "/api/chatbot/**").hasAnyAuthority("ATUALIZAR_CHATBOT", "ATUALIZAR_CHATBOT_EMPRESAS")

                        //Relatorios
                        .requestMatchers(HttpMethod.GET, "/api/relatorios/**").hasAnyAuthority("VISUALIZAR_RELATORIOS", "VISUALIZAR_RELATORIOS_EQUIPE")

                        // Feedbacks
                        .requestMatchers(HttpMethod.GET, "/api/feedbacks/**").hasAuthority("VISUALIZAR_FEEDBACK_CLIENTES")

                        //Base de conhecimento
                        .requestMatchers(HttpMethod.GET, "/api/base-conhecimento").hasAuthority("ACESSAR_BASE_CONHECIMENTO")
                         .requestMatchers(HttpMethod.POST, "/api/base-conhecimento").hasAuthority("CRIAR_ARTIGO_BASE_CONHECIMENTO")
                        .requestMatchers(HttpMethod.PUT, "/api/base-conhecimento/**").hasAuthority("EDITAR_ARTIGO_BASE_CONHECIMENTO")


                        // Regra geral (todas as outras requisições exigem autenticação)
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}