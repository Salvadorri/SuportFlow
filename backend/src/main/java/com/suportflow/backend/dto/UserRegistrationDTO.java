// src/main/java/com/suportflow/backend/dto/UserRegistrationDTO.java
package com.suportflow.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    @Size(min = 8, message = "A senha deve ter pelo menos 6 caracteres")
    private String password;
    private String telefone; //add
    private String empresaNome; // Optional, could be null.  Make sure your service handles this.

    @NotBlank(message = "CPF/CNPJ é obrigatório")
    @Pattern(regexp = "^\\d{11}$|^\\d{14}$", message = "CPF/CNPJ inválido")
    private String cpfCnpj;
}