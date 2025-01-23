package com.suportflow.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "mensagens_chamado")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MensagemChamado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mensagem_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chamado_id", nullable = false)
    private Chamado chamado;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "mensagem", nullable = false)
    private String mensagem;

    @Column(name = "data_envio")
    private LocalDateTime dataEnvio = LocalDateTime.now();
}