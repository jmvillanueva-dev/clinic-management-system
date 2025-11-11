package com.clinic.webapi.modules.historiasclinicas.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class HistoriaClinicaResponse {
  private UUID id;
  private UUID pacienteId;
  private String pacienteCedula;
  private String pacienteNombreCompleto;
  private String numeroHistoriaClinica;
  private String institucionSistema;
  private String unidadOperativa;
  private String codUnidad;
  private Instant fechaCreacion;
  private Instant fechaActualizacion;
  private Integer totalEvoluciones;
}