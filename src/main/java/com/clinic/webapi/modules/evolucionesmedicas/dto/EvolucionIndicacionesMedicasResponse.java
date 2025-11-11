package com.clinic.webapi.modules.evolucionesmedicas.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class EvolucionIndicacionesMedicasResponse {
  private UUID id;
  private UUID planTratamientoId;
  private String medicamento;
  private String dosis;
  private String frecuencia;
  private String viaAdministracion;
  private String duracion;
  private String indicacionesEspeciales;
  private Instant fechaCreacion;
}