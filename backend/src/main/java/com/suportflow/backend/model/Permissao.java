package com.suportflow.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "permissoes")
public class Permissao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permissao_id")
    private Long id;

    @Column(name = "nome", nullable = false, unique = true) // Corrigido para "nome"
    private String nome;

    @Column(name = "descricao")
    private String descricao;

    // Construtores
    public Permissao() {}

    // Getters e Setters

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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}