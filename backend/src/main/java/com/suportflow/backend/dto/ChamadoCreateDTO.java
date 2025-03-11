// src/main/java/com/suportflow/backend/dto/ChamadoCreateDTO.java
package com.suportflow.backend.dto;

import com.suportflow.backend.model.CategoriaChamado;
import com.suportflow.backend.model.PrioridadeChamado;
import com.suportflow.backend.model.StatusChamado;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ChamadoCreateDTO {

    @NotBlank(message = "O título do chamado é obrigatório.")
    @Schema(description = "Ticket Title", example = "Example Support Ticket")
    private String titulo;

    @Schema(description = "Ticket Description", example = "Detailed problem description")
    private String descricao;

    @NotNull(message = "A categoria do chamado é obrigatória.")
    @Schema(description = "Ticket Category", example = "HARDWARE")
    private CategoriaChamado categoria;

    @NotNull(message = "A prioridade do chamado é obrigatória.")
    @Schema(description = "Ticket Priority", example = "ALTA")
    private PrioridadeChamado prioridade;

    @Schema(description = "Ticket Status", example = "ABERTO", defaultValue = "ABERTO")
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