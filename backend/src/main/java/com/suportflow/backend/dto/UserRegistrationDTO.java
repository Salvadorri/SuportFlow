// src/main/java/com/suportflow/backend/dto/UserRegistrationDTO.java
package com.suportflow.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDTO {

  @NotBlank(message = "O nome é obrigatório")
  private String nome;

  @NotBlank(message = "O email é obrigatório")
  @Email(message = "Email inválido")
  private String email;

  @NotBlank(message = "A senha é obrigatória")
  @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres") // Corrigido para 6
  private String password;

  private String telefone;
  private String empresaNome;

  @Pattern(regexp = "^\\d{11}$|^\\d{14}$", message = "CPF/CNPJ inválido")
  private String cpfCnpj;

    @NotEmpty(message = "Pelo menos uma permissão deve ser informada") //Valida se a lista não é vazia
    private List<String> roles;
}