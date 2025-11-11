package com.clinic.webapi.modules.evolucionesmedicas.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EvolucionMotivoAtencionRequest {
  @NotBlank(message = "El motivo de consulta es obligatorio")
  private String motivoConsulta;

  private String enfermedadActual;
}