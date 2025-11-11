package com.clinic.webapi.modules.evolucionesmedicas.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class EvolucionValoracionClinicaResponse {
  private UUID id;
  private UUID evolucionMedicaId;
  private String inspeccionGeneral;
  private String cabezaCuello;
  private String torax;
  private String abdomen;
  private String extremidades;
  private String neurologico;
  private String pielTegumentos;
  private String otrosHallazgos;
  private Instant fechaCreacion;
}