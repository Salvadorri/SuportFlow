package com.suportflow.backend.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "usuarios")
public class User implements UserDetails { // 1. Implementa UserDetails

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "senha", nullable = false)
    private String senha; // Renomeado para 'senha' para consistência

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @Column(name = "ativo")
    private Boolean ativo;

    @ManyToMany(fetch = FetchType.EAGER) // MUITO IMPORTANTE: Use FetchType.EAGER
    @JoinTable(
            name = "usuario_permissao",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "permissao_id")
    )
    private Set<Permissao> permissoes = new HashSet<>();

    // Getters e Setters (como antes, mas com 'senha' em vez de 'password')

    // Métodos da interface UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 2. Mapeia Permissao para GrantedAuthority
        return permissoes.stream()
                .map(permissao -> new SimpleGrantedAuthority(permissao.getNome())) // Usa o nome da permissão
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.senha; // Retorna a senha
    }

    @Override
    public String getUsername() {
        return this.email; // Retorna o email (usado como username)
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Modifique se tiver lógica de expiração de conta
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Modifique se tiver lógica de bloqueio de conta
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Modifique se tiver lógica de expiração de credenciais
    }

    @Override
    public boolean isEnabled() {
        return this.ativo; // Usa o campo 'ativo'
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getSenha() { // Corrigido o nome do getter para 'getSenha'
        return senha;
    }

    public void setSenha(String senha) { // Corrigido o nome do setter para 'setSenha'
        this.senha = senha;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
    public Set<Permissao> getPermissoes() {
        return permissoes;
    }

    public void setPermissoes(Set<Permissao> permissoes) {
        this.permissoes = permissoes;
    }
}