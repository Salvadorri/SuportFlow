package com.suportflow.backend.dto;

import com.suportflow.backend.model.Cliente;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClienteDTO {
  private Long id;
  private String nome;
  private String email;
  private String telefone;
  private String cpfCnpj;
  private String empresaNome;
  private LocalDateTime dataCadastro;
  private boolean ativo;

  public ClienteDTO(Cliente cliente) {
    this.id = cliente.getId();
    this.nome = cliente.getNome();
    this.email = cliente.getEmail();
    this.telefone = cliente.getTelefone();
    this.cpfCnpj = cliente.getCpfCnpj();
    if (cliente.getEmpresa() != null) {
      this.empresaNome = cliente.getEmpresa().getNome();
    }
    this.dataCadastro = cliente.getDataCadastro();
    this.ativo = cliente.isAtivo();
  }
}