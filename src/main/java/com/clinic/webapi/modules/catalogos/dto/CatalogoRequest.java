package com.clinic.webapi.modules.catalogos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CatalogoRequest {

  @NotBlank(message = "El nombre del catálogo es obligatorio")
  private String nombre;

  private String descripcion;

  @NotBlank(message = "El tipo de catálogo es obligatorio")
  private String tipo; // SISTEMA, MEDICO

  private Boolean estaActivo;
}