package com.suportflow.backend.dto;

import com.suportflow.backend.model.Permissao;
import com.suportflow.backend.model.User;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserDetailsDTO {
    private Long id;
    private String nome;
    private String email;
    private String empresaNome;
    private boolean ativo;
    private List<String> permissoes;

    // Construtores, Getters e Setters

    public UserDetailsDTO() {
    }

    //Construtor que aceita um objeto User e popula os campos do DTO.
    public UserDetailsDTO(User user) {
        this.id = user.getId();
        this.nome = user.getNome();
        this.email = user.getEmail();
        this.empresaNome = user.getEmpresa() != null ? user.getEmpresa().getNome() : null;
        this.ativo = user.getAtivo() != null ? user.getAtivo() : false; //Considera false como padrão se for null
        this.permissoes = user.getPermissoes().stream()
                .map(Permissao::getNome)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getEmpresaNome() {
        return empresaNome;
    }

    public void setEmpresaNome(String empresaNome) {
        this.empresaNome = empresaNome;
    }
    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public List<String> getPermissoes() {
        return permissoes;
    }

    public void setPermissoes(List<String> permissoes) {
        this.permissoes = permissoes;
    }
}