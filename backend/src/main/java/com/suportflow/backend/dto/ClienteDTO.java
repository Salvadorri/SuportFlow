// src/main/java/com/suportflow/backend/dto/ClienteDTO.java
package com.suportflow.backend.dto;

import com.suportflow.backend.model.Cliente;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;

@Getter
@Setter
@NoArgsConstructor
public class ClienteDTO {
  private Long id;
  private String nome;
  private String email;
  private String telefone;
  private String cpfCnpj; // Could be CPF or CNPJ. Consider separate DTOs if you need specific
  // validation.
  private String empresaNome; // Or an EmpresaDTO if you need more details
  private LocalDateTime dataCadastro;
  private boolean ativo;

  // Constructor to create from the entity
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