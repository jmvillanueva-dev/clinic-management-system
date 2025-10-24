package com.clinic.webapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RolRequest {

  @NotBlank(message = "El nombre del rol es obligatorio.")
  @Size(max = 50, message = "El nombre no puede exceder los 50 caracteres.")
  private String nombre;

  @NotBlank(message = "El área del rol es obligatoria.")
  @Size(max = 100, message = "El área no puede exceder los 100 caracteres.")
  private String area;

  private String descripcion;
}