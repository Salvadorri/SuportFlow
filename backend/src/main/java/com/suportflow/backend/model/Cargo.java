package com.suportflow.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "cargos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(name = "is_admin", columnDefinition = "boolean default false")
    private boolean isAdmin;

    @Column(name = "is_gerente", columnDefinition = "boolean default false")
    private boolean isGerente;

    @Column(name = "pode_ver_todos_chamados", columnDefinition = "boolean default false")
    private boolean podeVerTodosChamados;

    @Column(name = "pode_criar_chamados", columnDefinition = "boolean default false")
    private boolean podeCriarChamados;

    @Column(name = "pode_responder_chamados", columnDefinition = "boolean default false")
    private boolean podeResponderChamados;

    @Column(name = "pode_fechar_chamados", columnDefinition = "boolean default false")
    private boolean podeFecharChamados;

    @Column(name = "pode_atribuir_chamados", columnDefinition = "boolean default false")
    private boolean podeAtribuirChamados;

    @Column(name = "pode_gerenciar_base_conhecimento", columnDefinition = "boolean default false")
    private boolean podeGerenciarBaseConhecimento;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @OneToMany(mappedBy = "cargo")
    private List<Usuario> usuarios;
}