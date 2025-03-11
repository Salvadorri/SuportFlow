// src/main/java/com/suportflow/backend/dto/ChamadoUpdateDTO.java
package com.suportflow.backend.dto;

import com.suportflow.backend.model.CategoriaChamado;
import com.suportflow.backend.model.PrioridadeChamado;
import com.suportflow.backend.model.StatusChamado;
import io.swagger.v3.oas.annotations.media.Schema;

public class ChamadoUpdateDTO {

    @Schema(description = "Ticket Title", example = "Updated Support Ticket")
    private String titulo;
    @Schema(description = "Ticket Description", example = "Updated problem description")
    private String descricao;
    @Schema(description = "Ticket Category", example = "SOFTWARE")
    private CategoriaChamado categoria;
    @Schema(description = "Ticket Priority", example = "MEDIA")
    private PrioridadeChamado prioridade;
    @Schema(description = "Ticket Status", example = "EM_ANDAMENTO")
    private StatusChamado status;
    @Schema(description = "Atendente ID", example = "3")
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