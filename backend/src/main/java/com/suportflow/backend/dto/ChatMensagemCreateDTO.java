// src/main/java/com/suportflow/backend/dto/ChatMensagemCreateDTO.java
package com.suportflow.backend.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ChatMensagemCreateDTO {

    @NotNull(message = "O ID do chamado é obrigatório.")
    private Long chamadoId;

    private Long usuarioId;
    private Long clienteId;

    @NotBlank(message = "A mensagem não pode estar vazia.")
    @Size(max = 500, message = "A mensagem deve ter no máximo 500 caracteres.")
    private String mensagem;

    private String caminhoArquivo; // ADDED: Store the file path

    // Getters and Setters (include getter and setter for caminhoArquivo)

    public Long getChamadoId() {
        return chamadoId;
    }

    public void setChamadoId(Long chamadoId) {
        this.chamadoId = chamadoId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getCaminhoArquivo() {
        return caminhoArquivo;
    }

    public void setCaminhoArquivo(String caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
    }
}