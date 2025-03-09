// src/main/java/com/suportflow/backend/dto/UserRegistrationDTO.java
package com.suportflow.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
    @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres")
    private String password;
    private String telefone; //add
    private String empresaNome; // Optional, could be null.  Make sure your service handles this.

    @CPF(message = "CPF inválido") // Validate as CPF by default
    @CNPJ(message = "CNPJ inválido")  // *Also* validate as CNPJ.  This will fail if *both* fail.
    private String cpfCnpj;
}