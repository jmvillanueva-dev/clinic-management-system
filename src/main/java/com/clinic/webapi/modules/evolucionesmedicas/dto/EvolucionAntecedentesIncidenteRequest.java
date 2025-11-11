package com.clinic.webapi.modules.evolucionesmedicas.dto;

import lombok.Data;

@Data
public class EvolucionAntecedentesIncidenteRequest {
  private String antecedentesPersonales;
  private String antecedentesFamiliares;
  private String habitosToxicos;
  private String alergias;
  private String medicamentosActuales;
}