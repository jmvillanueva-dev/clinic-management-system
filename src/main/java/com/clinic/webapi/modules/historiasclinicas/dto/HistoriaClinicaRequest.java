package com.clinic.webapi.modules.historiasclinicas.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class HistoriaClinicaRequest {
  private UUID pacienteId;
  private String institucionSistema;
  private String unidadOperativa;
  private String codUnidad;
}