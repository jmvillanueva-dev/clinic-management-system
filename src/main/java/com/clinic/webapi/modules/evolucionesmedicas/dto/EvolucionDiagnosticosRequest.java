package com.clinic.webapi.modules.evolucionesmedicas.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EvolucionDiagnosticosRequest {
  private String codigoCie;

  @NotBlank(message = "El diagnóstico es obligatorio")
  private String diagnostico;

  private String tipo;
  private String observaciones;
}