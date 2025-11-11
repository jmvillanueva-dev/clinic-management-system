package com.clinic.webapi.modules.evolucionesmedicas.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class EvolucionAntecedentesIncidenteResponse {
  private UUID id;
  private UUID evolucionMedicaId;
  private String antecedentesPersonales;
  private String antecedentesFamiliares;
  private String habitosToxicos;
  private String alergias;
  private String medicamentosActuales;
  private Instant fechaCreacion;
}