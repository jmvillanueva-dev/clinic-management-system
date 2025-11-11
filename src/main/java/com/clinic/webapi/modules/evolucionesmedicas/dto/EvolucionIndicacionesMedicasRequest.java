package com.clinic.webapi.modules.evolucionesmedicas.dto;

import lombok.Data;

@Data
public class EvolucionIndicacionesMedicasRequest {
  private String medicamento;
  private String dosis;
  private String frecuencia;
  private String viaAdministracion;
  private String duracion;
  private String indicacionesEspeciales;
}