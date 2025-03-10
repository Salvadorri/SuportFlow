// src/main/java/com/suportflow/backend/dto/ChamadoUpdateDTO.java
package com.suportflow.backend.dto;

import com.suportflow.backend.model.CategoriaChamado;
import com.suportflow.backend.model.PrioridadeChamado;
import com.suportflow.backend.model.StatusChamado;

public class ChamadoUpdateDTO {

    private String titulo;
    private String descricao;
    private CategoriaChamado categoria;
    private PrioridadeChamado prioridade;
    private StatusChamado status;
    private Long atendenteId; // Allow updating the assigned attendant

    // Getters and setters

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

    public PrioridadeChamado getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(PrioridadeChamado prioridade) {
        this.prioridade = prioridade;
    }

    public StatusChamado getStatus() {
        return status;
    }

    public void setStatus(StatusChamado status) {
        this.status = status;
    }

    public Long getAtendenteId() {
        return atendenteId;
    }

    public void setAtendenteId(Long atendenteId) {
        this.atendenteId = atendenteId;
    }
}
