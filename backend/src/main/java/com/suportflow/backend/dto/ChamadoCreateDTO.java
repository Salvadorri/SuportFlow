// src/main/java/com/suportflow/backend/dto/ChamadoCreateDTO.java
package com.suportflow.backend.dto;

import com.suportflow.backend.model.CategoriaChamado;
import com.suportflow.backend.model.PrioridadeChamado;
import com.suportflow.backend.model.StatusChamado;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ChamadoCreateDTO {

    @NotBlank(message = "O título do chamado é obrigatório.")
    private String titulo;

    private String descricao;

    @NotNull(message = "A categoria do chamado é obrigatória.")
    private CategoriaChamado categoria;

    @NotNull(message = "A prioridade do chamado é obrigatória.")
    private PrioridadeChamado prioridade;

    private StatusChamado status = StatusChamado.ABERTO; // Default to OPEN

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

}