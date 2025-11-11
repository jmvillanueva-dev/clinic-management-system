package com.clinic.webapi.modules.evolucionesmedicas.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class EvolucionMedicaResumenResponse {
  private UUID id;
  private UUID historiaClinicaId;
  private String numeroHistoriaClinica;
  private UUID empleadoId;
  private String empleadoNombreCompleto;
  private Instant fechaConsulta;
  private String tipoConsulta;
  private String estado;
  private Instant fechaCreacion;
  private boolean tieneSignosVitales;
  private boolean tieneDiagnosticos;
  private boolean tieneTratamientos;
}