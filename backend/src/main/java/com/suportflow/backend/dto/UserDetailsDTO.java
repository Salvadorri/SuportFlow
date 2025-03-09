// src/main/java/com/suportflow/backend/dto/UserDetailsDTO.java
package com.suportflow.backend.dto;

import com.suportflow.backend.model.User;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDetailsDTO {

  private Long id;
  private String nome;
  private String email;
  private String empresaNome; // Or an EmpresaDTO if you need more details
  private LocalDateTime dataCriacao;
  private Boolean ativo;
  private String telefone;       // Added
  private String cpfCnpj;      // Added


  public UserDetailsDTO(User user) {
    this.id = user.getId();
    this.nome = user.getNome();
    this.email = user.getEmail();
    if (user.getEmpresa() != null) {
      this.empresaNome = user.getEmpresa().getNome();
    }
    this.dataCriacao = user.getDataCriacao();
    this.ativo = user.getAtivo();
    this.telefone = user.getTelefone();   // Added
    this.cpfCnpj = user.getCpfCnpj();      // Added
  }
}