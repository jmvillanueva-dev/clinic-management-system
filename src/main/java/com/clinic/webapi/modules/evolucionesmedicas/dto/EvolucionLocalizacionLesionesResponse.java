package com.clinic.webapi.modules.evolucionesmedicas.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class EvolucionLocalizacionLesionesResponse {
  private UUID id;
  private UUID evolucionMedicaId;
  private String localizacion;
  private String tipoLesion;
  private String descripcion;
  private String gravedad;
  private Instant fechaCreacion;
}