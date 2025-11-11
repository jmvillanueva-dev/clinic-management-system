package com.clinic.webapi.modules.evolucionesmedicas.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class EvolucionEmergenciaObstetricaResponse {
  private UUID id;
  private UUID evolucionMedicaId;
  private Integer gestasPrevias;
  private Integer partosPrevios;
  private Integer abortosPrevios;
  private LocalDate fum;
  private LocalDate fpp;
  private Integer semanasGestacion;
  private String presentacion;
  private String dinamicaUterina;
  private String latidosFetales;
  private String observaciones;
  private Instant fechaCreacion;
}