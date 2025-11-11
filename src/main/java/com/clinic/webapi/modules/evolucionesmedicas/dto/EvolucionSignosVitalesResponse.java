package com.clinic.webapi.modules.evolucionesmedicas.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class EvolucionSignosVitalesResponse {
  private UUID id;
  private UUID evolucionMedicaId;
  private Integer presionArterialSistolica;
  private Integer presionArterialDiastolica;
  private Integer frecuenciaCardiaca;
  private Integer frecuenciaRespiratoria;
  private BigDecimal temperatura;
  private Integer saturacionOxigeno;
  private BigDecimal peso;
  private BigDecimal talla;
  private BigDecimal imc;
  private BigDecimal glucosa;
  private Instant fechaCreacion;
}