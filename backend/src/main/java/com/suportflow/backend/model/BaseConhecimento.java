package com.suportflow.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "base_conhecimento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseConhecimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "artigo_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "conteudo", nullable = false)
    private String conteudo;

    @Column(name = "palavras_chave")
    private String palavrasChave;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;
}
