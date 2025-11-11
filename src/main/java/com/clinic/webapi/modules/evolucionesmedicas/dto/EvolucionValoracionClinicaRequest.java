package com.clinic.webapi.modules.evolucionesmedicas.dto;

import lombok.Data;

@Data
public class EvolucionValoracionClinicaRequest {
  private String inspeccionGeneral;
  private String cabezaCuello;
  private String torax;
  private String abdomen;
  private String extremidades;
  private String neurologico;
  private String pielTegumentos;
  private String otrosHallazgos;
}