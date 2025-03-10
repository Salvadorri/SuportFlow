package com.suportflow.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClienteRegistrationDTO {

  @NotBlank(message = "O nome é obrigatório")
  @Size(min = 2, max = 255, message = "O nome deve ter entre 2 e 255 caracteres")
  private String nome;

  @NotBlank(message = "O email é obrigatório")
  @Email(message = "Email inválido")
  private String email;

  @NotBlank(message = "O telefone é obrigatório.")
  private String telefone;

  @NotBlank(message = "O CPF/CNPJ é obrigatório")
  @Pattern(regexp = "^\\d{11}$|^\\d{14}$", message = "CPF/CNPJ inválido")
  private String cpfCnpj;

  private String empresaNome;

  @NotBlank(message = "A senha é obrigatória")
  @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres")
  private String senha;
}