package com.clinic.webapi.modules.evolucionesmedicas.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class EvolucionMedicaResponse {
  private UUID id;
  private UUID historiaClinicaId;
  private String numeroHistoriaClinica;
  private UUID empleadoId;
  private String empleadoNombreCompleto;
  private String empleadoEspecialidad;
  private Instant fechaConsulta;
  private String tipoConsulta;
  private String estado;
  private String observacionesGenerales;
  private Instant fechaCreacion;
  private Instant fechaActualizacion;

  // Secciones
  private EvolucionMotivoAtencionResponse motivoAtencion;
  private EvolucionAntecedentesIncidenteResponse antecedentesIncidente;
  private EvolucionSignosVitalesResponse signosVitales;
  private EvolucionValoracionClinicaResponse valoracionClinica;
  private List<EvolucionLocalizacionLesionesResponse> localizacionLesiones;
  private List<EvolucionExamenesSolicitadosResponse> examenesSolicitados;
  private List<EvolucionDiagnosticosResponse> diagnosticos;
  private List<EvolucionPlanesTratamientoResponse> planesTratamiento;
  private EvolucionEmergenciaObstetricaResponse emergenciaObstetrica;
  private EvolucionAltaMedicaResponse altaMedica;
}