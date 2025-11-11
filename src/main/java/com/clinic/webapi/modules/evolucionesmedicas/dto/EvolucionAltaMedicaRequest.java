package com.clinic.webapi.modules.evolucionesmedicas.dto;

import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;

@Data
public class EvolucionAltaMedicaRequest {
  private Instant fechaAlta;
  private String tipoAlta;
  private String condicionAlta;
  private String recomendaciones;
  private LocalDate controlProgramado;
  private String especialidadControl;
}