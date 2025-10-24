package com.clinic.webapi.dto;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Data
public class UsuarioEmailUpdateRequest {
  @NotBlank(message = "El nuevo email es obligatorio.")
  @Email(message = "El formato del nuevo email es inválido.")
  private String nuevoEmail;
}