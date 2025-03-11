package com.suportflow.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteUpdateDTO {

    @Size(min = 2, max = 255, message = "O nome deve ter entre 2 e 255 caracteres")
    private String nome;

    @Email(message = "Email inválido")
    private String email;

    private String telefone;

    @Pattern(regexp = "^\\d{11}$|^\\d{14}$", message = "CPF/CNPJ inválido") //Validação
    private String cpfCnpj;

    private String empresaNome;
}