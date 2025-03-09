// src/main/java/com/suportflow/backend/dto/UserUpdateDTO.java
package com.suportflow.backend.dto;

import jakarta.validation.constraints.Email;
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
public class UserUpdateDTO {

    private String nome; // No @NotBlank here

    @Email(message = "Email inválido")
    private String email; // No @NotBlank

    @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres")
    private String password; // No @NotBlank.  Still validate size if present.

    private String telefone;
    private String empresaNome;

    private String cpfCnpj;
}