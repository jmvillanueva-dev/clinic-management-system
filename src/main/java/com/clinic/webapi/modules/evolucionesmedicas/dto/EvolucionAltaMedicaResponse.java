package com.clinic.webapi.modules.evolucionesmedicas.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class EvolucionAltaMedicaResponse {
  private UUID id;
  private UUID evolucionMedicaId;
  private Instant fechaAlta;
  private String tipoAlta;
  private String condicionAlta;
  private String recomendaciones;
  private LocalDate controlProgramado;
  private String especialidadControl;
  private Instant fechaCreacion;
}