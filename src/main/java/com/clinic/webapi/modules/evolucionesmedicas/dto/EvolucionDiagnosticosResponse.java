package com.clinic.webapi.modules.evolucionesmedicas.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class EvolucionDiagnosticosResponse {
  private UUID id;
  private UUID evolucionMedicaId;
  private String codigoCie;
  private String diagnostico;
  private String tipo;
  private String observaciones;
  private Instant fechaCreacion;
}