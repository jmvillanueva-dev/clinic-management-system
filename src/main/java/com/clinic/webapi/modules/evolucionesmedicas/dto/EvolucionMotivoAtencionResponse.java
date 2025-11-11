package com.clinic.webapi.modules.evolucionesmedicas.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class EvolucionMotivoAtencionResponse {
  private UUID id;
  private UUID evolucionMedicaId;
  private String motivoConsulta;
  private String enfermedadActual;
  private Instant fechaCreacion;
}