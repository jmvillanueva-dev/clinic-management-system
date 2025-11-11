package com.clinic.webapi.modules.evolucionesmedicas.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EvolucionEmergenciaObstetricaRequest {
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
}