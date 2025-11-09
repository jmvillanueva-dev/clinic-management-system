package com.clinic.webapi.modules.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthRequest {

  @NotBlank(message = "El email no puede estar vacío.")
  @Email(message = "El formato del email es inválido.")
  private String email;

  @NotBlank(message = "La contraseña no puede estar vacía.")
  private String password;
}