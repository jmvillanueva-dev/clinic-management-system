package com.clinic.webapi.modules.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForgotPasswordRequest {
    @NotBlank(message = "El email es obligatorio.")
    @Email(message = "El formato del email es inválido.")
    private String email;
}
