package com.suportflow.backend.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "permissoes")
public class Permissao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permissao_id")
    private Long id;

    @Column(name = "nome", nullable = false, unique = true)
    private String nome;

    @Column(name = "descricao")
    private String descricao;

    @ManyToMany(mappedBy = "permissoes")
    private Set<User> usuarios = new HashSet<>();

    public Set<User> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Set<User> usuarios) {
        this.usuarios = usuarios;
    }


    public void addUsuario(User usuario) {
        this.usuarios.add(usuario);
        usuario.getPermissoes().add(this); // Mantém a consistência bidirecional
    }

    public void removeUsuario(User usuario) {
        this.usuarios.remove(usuario);
        usuario.getPermissoes().remove(this); // Mantém a consistência bidirecional
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}