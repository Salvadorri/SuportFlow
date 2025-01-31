package com.suportflow.backend.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;

// Enum para as Permissões
enum Permissao {
    ADMIN,
    GERENTE,
    VER_TODOS_CHAMADOS,
    CRIAR_CHAMADOS,
    RESPONDER_CHAMADOS,
    FECHAR_CHAMADOS,
    ATRIBUIR_CHAMADOS,
    GERENCIAR_BASE_CONHECIMENTO
}

@Entity
@Table(name = "cargos")
public class Cargo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cargo_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @Column(name = "nome_cargo", nullable = false)
    private String nomeCargo;

    @Column(name = "descricao")
    private String descricao;

    @ElementCollection(targetClass = Permissao.class)
    @CollectionTable(name = "cargo_permissao", joinColumns = @JoinColumn(name = "cargo_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "permissao")
    private Set<Permissao> permissoes;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    // Construtores
    public Cargo() {
    }

    // Getters e Setters

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

    public String getNomeCargo() {
        return nomeCargo;
    }

    public void setNomeCargo(String nomeCargo) {
        this.nomeCargo = nomeCargo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Set<Permissao> getPermissoes() {
        return permissoes;
    }

    public void setPermissoes(Set<Permissao> permissoes) {
        this.permissoes = permissoes;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}