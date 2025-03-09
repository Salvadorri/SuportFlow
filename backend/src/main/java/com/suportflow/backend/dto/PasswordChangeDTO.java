// src/main/java/com/suportflow/backend/dto/PasswordChangeDTO.java
package com.suportflow.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChangeDTO {

    @NotBlank(message = "A senha antiga é obrigatória")
    private String oldPassword;

    @NotBlank(message = "A nova senha é obrigatória")
    @Size(min = 6, message = "A nova senha deve ter pelo menos 6 caracteres")
    private String newPassword;

    @NotBlank(message = "A confirmação da nova senha é obrigatória")
    private String confirmNewPassword;
}