package com.clinic.webapi.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class UsuarioPasswordUpdateRequest {

  @NotBlank(message = "La contraseña actual es obligatoria.")
  private String contrasenaActual;

  @NotBlank(message = "La nueva contraseña es obligatoria.")
  @Size(min = 8, message = "La nueva contraseña debe tener al menos 8 caracteres.")
  private String nuevaContrasena;
}