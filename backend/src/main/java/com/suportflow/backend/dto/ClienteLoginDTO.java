// ClienteLoginDTO.java (NOVO)
package com.suportflow.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

public class ClienteLoginDTO {

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "O CPF/CNPJ é obrigatório")
    private String cpfCnpj; // Usaremos cpfCnpj como "senha" para clientes


    // Getters e setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }
}