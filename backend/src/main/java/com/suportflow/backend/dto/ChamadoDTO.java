// src/main/java/com/suportflow/backend/dto/ChamadoDTO.java
package com.suportflow.backend.dto;

import com.suportflow.backend.model.CategoriaChamado;
import com.suportflow.backend.model.PrioridadeChamado;
import com.suportflow.backend.model.StatusChamado;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public class ChamadoDTO {
    @Schema(description = "Chamado ID", example = "1")
    private Long id;
    @Schema(description = "Client ID", example = "1")
    private Long clienteId;
    @Schema(description = "Client Name", example = "John Doe")
    private String clienteNome; // Include client name for easier display
    @Schema(description = "Atendente ID", example = "2")
    private Long atendenteId;
    @Schema(description = "Atendente Name", example = "Jane Smith")
    private String atendenteNome;
    @Schema(description = "Ticket Title", example = "Example Support Ticket")
    private String titulo;
    @Schema(description = "Ticket Description", example = "Detailed problem description")
    private String descricao;
    @Schema(description = "Ticket Category", example = "HARDWARE")
    private CategoriaChamado categoria;
    @Schema(description = "Ticket Status", example = "ABERTO")
    private StatusChamado status;
    @Schema(description = "Ticket Priority", example = "ALTA")
    private PrioridadeChamado prioridade;
    @Schema(description = "Ticket Open Date", example = "2024-01-01T10:00:00")
    private LocalDateTime dataAbertura;
    @Schema(description = "Ticket Close Date", example = "2024-01-02T15:00:00")
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