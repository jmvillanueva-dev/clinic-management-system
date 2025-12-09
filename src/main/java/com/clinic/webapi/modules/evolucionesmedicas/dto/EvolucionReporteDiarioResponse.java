package com.clinic.webapi.modules.evolucionesmedicas.dto;

import lombok.Builder;
import lombok.Data;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class EvolucionReporteDiarioResponse {
  // Datos de la Evolución
  private UUID evolucionId;
  private Instant fechaConsulta;
  
  // Datos de la Historia Clínica
  private UUID historiaClinicaId;
  private String numeroHistoriaClinica;

  // Datos del Paciente
  private UUID pacienteId;
  private String pacienteNombreCompleto;

  // --- NUEVO CAMPO ---
  // Datos del Profesional (Médico/Enfermero)
  private String empleadoNombreCompleto;
}
