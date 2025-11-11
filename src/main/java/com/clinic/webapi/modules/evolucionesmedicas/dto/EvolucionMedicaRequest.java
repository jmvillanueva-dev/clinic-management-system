package com.clinic.webapi.modules.evolucionesmedicas.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
public class EvolucionMedicaRequest {
  @NotNull(message = "El ID de la historia clínica es obligatorio")
  private UUID historiaClinicaId;

  @NotNull(message = "El ID del empleado (médico) es obligatorio")
  private UUID empleadoId;

  private Instant fechaConsulta;
  private String tipoConsulta;
  private String estado;
  private String observacionesGenerales;

  // Secciones opcionales
  private EvolucionMotivoAtencionRequest motivoAtencion;
  private EvolucionAntecedentesIncidenteRequest antecedentesIncidente;
  private EvolucionSignosVitalesRequest signosVitales;
  private EvolucionValoracionClinicaRequest valoracionClinica;
  private List<EvolucionLocalizacionLesionesRequest> localizacionLesiones;
  private List<EvolucionExamenesSolicitadosRequest> examenesSolicitados;
  private List<EvolucionDiagnosticosRequest> diagnosticos;
  private List<EvolucionPlanesTratamientoRequest> planesTratamiento;
  private EvolucionEmergenciaObstetricaRequest emergenciaObstetrica;
  private EvolucionAltaMedicaRequest altaMedica;
}