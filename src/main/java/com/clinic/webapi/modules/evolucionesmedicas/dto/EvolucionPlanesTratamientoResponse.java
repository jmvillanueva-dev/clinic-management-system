package com.clinic.webapi.modules.evolucionesmedicas.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class EvolucionPlanesTratamientoResponse {
  private UUID id;
  private UUID evolucionMedicaId;
  private String nombreTratamiento;
  private String descripcion;
  private String tipoTratamiento;
  private String duracion;
  private Instant fechaCreacion;
  private List<EvolucionIndicacionesMedicasResponse> indicacionesMedicas;
}