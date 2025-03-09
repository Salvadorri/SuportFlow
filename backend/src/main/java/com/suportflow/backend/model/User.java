// src/main/java/com/suportflow/backend/model/User.java
package com.suportflow.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "usuarios")
public class User implements UserDetails {

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
  private String senha;

  @Column(name = "data_criacao")
  private LocalDateTime dataCriacao;

  @Column(name = "ativo")
  private Boolean ativo;

  @Column(name = "telefone") // Added
  private String telefone;

  @Column(
      name = "cpf_cnpj",
      unique = true) // Added, assuming CPF/CNPJ should also be unique
  private String cpfCnpj;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "usuario_permissao",
      joinColumns = @JoinColumn(name = "usuario_id"),
      inverseJoinColumns = @JoinColumn(name = "permissao_id"))
  private Set<Permissao> permissoes = new HashSet<>(); // Mantém o HashSet para persistência

  // Métodos da interface UserDetails
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    // Ordena as permissões alfabeticamente antes de mapear para GrantedAuthority
    List<Permissao> sortedPermissoes = new ArrayList<>(permissoes); // Cria uma lista a partir do Set
    Collections.sort(sortedPermissoes, Comparator.comparing(Permissao::getNome)); // Ordena a lista

    return sortedPermissoes.stream()
        .map(permissao -> new SimpleGrantedAuthority(permissao.getNome()))
        .collect(Collectors.toList());
  }

  @Override
  public String getPassword() {
    return this.senha;
  }

  @Override
  public String getUsername() {
    return this.email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return this.ativo;
  }

  // Getters and Setters
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

  public String getSenha() {
    return senha;
  }

  public void setSenha(String senha) {
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

  public String getTelefone() {
    return telefone;
  }

  public void setTelefone(String telefone) {
    this.telefone = telefone;
  }

  public String getCpfCnpj() {
    return cpfCnpj;
  }

  public void setCpfCnpj(String cpfCnpj) {
    this.cpfCnpj = cpfCnpj;
  }
}