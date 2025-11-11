package com.clinic.webapi.modules.evolucionesmedicas.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class EvolucionExamenesSolicitadosResponse {
  private UUID id;
  private UUID evolucionMedicaId;
  private String tipoExamen;
  private String nombreExamen;
  private String indicaciones;
  private String urgencia;
  private String estado;
  private Instant fechaCreacion;
}