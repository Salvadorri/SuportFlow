package com.suportflow.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Entity
@Table(name = "clientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @Column(name = "cliente_id")
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "cliente_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @Column(name = "telefone", length = 20)
    private String telefone;

    @Column(name = "outras_informacoes", columnDefinition = "jsonb")
    private String outrasInformacoes;

    @OneToMany(mappedBy = "cliente")
    private List<Chamado> chamados;

    @OneToMany(mappedBy = "cliente")
    private List<Feedback> feedbacks;
}