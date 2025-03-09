// src/main/java/com/suportflow/backend/dto/ClienteUpdateDTO.java
//For partial updates
package com.suportflow.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClienteUpdateDTO {

  @Size(min = 2, max = 255, message = "O nome deve ter entre 2 e 255 caracteres")
  private String nome; // Optional for updates

  @Email(message = "Email inválido")
  private String email; // Optional

  private String telefone; // Optional

  private String cpfCnpj; // Optional, but if provided, should be validated

  private String empresaNome; // Optional
}