package com.clinic.webapi.modules.evolucionesmedicas.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EvolucionExamenesSolicitadosRequest {
  @NotBlank(message = "El tipo de examen es obligatorio")
  private String tipoExamen;

  @NotBlank(message = "El nombre del examen es obligatorio")
  private String nombreExamen;

  private String indicaciones;
  private String urgencia;
  private String estado;
}