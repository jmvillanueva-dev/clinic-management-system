package com.clinic.webapi.modules.evolucionesmedicas.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class EvolucionSignosVitalesRequest {
  private Integer presionArterialSistolica;
  private Integer presionArterialDiastolica;
  private Integer frecuenciaCardiaca;
  private Integer frecuenciaRespiratoria;
  private BigDecimal temperatura;
  private Integer saturacionOxigeno;
  private BigDecimal peso;
  private BigDecimal talla;
  private BigDecimal glucosa;
}