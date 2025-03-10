// src/main/java/com/suportflow/backend/dto/ChamadoDTO.java
package com.suportflow.backend.dto;

import com.suportflow.backend.model.CategoriaChamado;
import com.suportflow.backend.model.PrioridadeChamado;
import com.suportflow.backend.model.StatusChamado;

import java.time.LocalDateTime;

public class ChamadoDTO {
    private Long id;
    private Long clienteId;
    private String clienteNome; // Include client name for easier display
    private Long atendenteId;
    private String atendenteNome;
    private String titulo;
    private String descricao;
    private CategoriaChamado categoria;
    private StatusChamado status;
    private PrioridadeChamado prioridade;
    private LocalDateTime dataAbertura;
    private LocalDateTime dataFechamento;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public String getClienteNome() {
        return clienteNome;
    }

    public void setClienteNome(String clienteNome) {
        this.clienteNome = clienteNome;
    }

    public Long getAtendenteId() {
        return atendenteId;
    }

    public void setAtendenteId(Long atendenteId) {
        this.atendenteId = atendenteId;
    }

    public String getAtendenteNome() {
        return atendenteNome;
    }

    public void setAtendenteNome(String atendenteNome) {
        this.atendenteNome = atendenteNome;
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

    public CategoriaChamado getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaChamado categoria) {
        this.categoria = categoria;
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

}