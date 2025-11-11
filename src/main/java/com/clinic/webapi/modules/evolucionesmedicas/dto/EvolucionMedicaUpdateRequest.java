package com.clinic.webapi.modules.evolucionesmedicas.dto;

import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
public class EvolucionMedicaUpdateRequest {
  private Instant fechaConsulta;
  private String tipoConsulta;
  private String estado;
  private String observacionesGenerales;

  // Secciones opcionales para actualización
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