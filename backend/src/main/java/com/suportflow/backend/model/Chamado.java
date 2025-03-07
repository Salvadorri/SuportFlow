package com.suportflow.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "chamados")
public class Chamado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chamado_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente; // Cada chamado pertence a um cliente

    @ManyToOne
    @JoinColumn(name = "usuario_id")  //Pode ser nulo se não tiver sido atribuido
    private User atendente; // Atendente (User) responsável pelo chamado

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusChamado status; // Status do chamado (ver enum abaixo)

    @Enumerated(EnumType.STRING)
    @Column(name = "prioridade", nullable = false)
    private PrioridadeChamado prioridade; // Prioridade do chamado

    @Column(name = "data_abertura", nullable = false)
    private LocalDateTime dataAbertura;

    @Column(name = "data_fechamento")
    private LocalDateTime dataFechamento;  // Pode ser nulo se o chamado estiver aberto

    @Column(name = "avaliacao")
    private Integer avaliacao; // Avaliação do cliente (opcional)

    // Construtores
    public Chamado() {}

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public User getAtendente() {
        return atendente;
    }

    public void setAtendente(User atendente) {
        this.atendente = atendente;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public StatusChamado getStatus() {
        return status;
    }

    public void setStatus(StatusChamado status) {
        this.status = status;
    }

    public PrioridadeChamado getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(PrioridadeChamado prioridade) {
        this.prioridade = prioridade;
    }

    public LocalDateTime getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(LocalDateTime dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public LocalDateTime getDataFechamento() {
        return dataFechamento;
    }

    public void setDataFechamento(LocalDateTime dataFechamento) {
        this.dataFechamento = dataFechamento;
    }
    public Integer getAvaliacao() {
        return avaliacao;
    }
    public void setAvaliacao(Integer avaliacao) {
        this.avaliacao = avaliacao;
    }
}