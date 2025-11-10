package com.clinic.webapi.modules.catalogos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class ItemCatalogoRequest {

  @NotNull(message = "El ID del catálogo es obligatorio")
  private UUID catalogoId;

  @NotBlank(message = "El nombre del ítem es obligatorio")
  private String nombre;

  private String descripcion;

  private String codigo;

  private String valor;

  private Boolean estaActivo;
}