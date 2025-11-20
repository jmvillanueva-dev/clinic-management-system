package com.clinic.webapi.modules.evolucionesmedicas.model.mapper;

import com.clinic.webapi.modules.evolucionesmedicas.dto.*;
import com.clinic.webapi.modules.evolucionesmedicas.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EvolucionSeccionesMapper {

  // Mappings para EvolucionMotivoAtencion
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "evolucionMedica", ignore = true)
  @Mapping(target = "fechaCreacion", ignore = true)
  EvolucionMotivoAtencion toEntity(EvolucionMotivoAtencionRequest request);

  @Mapping(target = "evolucionMedicaId", source = "evolucionMedica.id")
  EvolucionMotivoAtencionResponse toResponse(EvolucionMotivoAtencion entity);

  // Mappings para EvolucionAntecedentesIncidente
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "evolucionMedica", ignore = true)
  @Mapping(target = "fechaCreacion", ignore = true)
  EvolucionAntecedentesIncidente toEntity(EvolucionAntecedentesIncidenteRequest request);

  @Mapping(target = "evolucionMedicaId", source = "evolucionMedica.id")
  EvolucionAntecedentesIncidenteResponse toResponse(EvolucionAntecedentesIncidente entity);

  // Mappings para EvolucionSignosVitales
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "evolucionMedica", ignore = true)
  @Mapping(target = "imc", ignore = true) // Se calcula automáticamente
  @Mapping(target = "fechaCreacion", ignore = true)
  EvolucionSignosVitales toEntity(EvolucionSignosVitalesRequest request);

  @Mapping(target = "evolucionMedicaId", source = "evolucionMedica.id")
  EvolucionSignosVitalesResponse toResponse(EvolucionSignosVitales entity);

  // Mappings para EvolucionValoracionClinica
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "evolucionMedica", ignore = true)
  @Mapping(target = "fechaCreacion", ignore = true)
  EvolucionValoracionClinica toEntity(EvolucionValoracionClinicaRequest request);

  @Mapping(target = "evolucionMedicaId", source = "evolucionMedica.id")
  EvolucionValoracionClinicaResponse toResponse(EvolucionValoracionClinica entity);

  // Mappings para EvolucionLocalizacionLesiones
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "evolucionMedica", ignore = true)
  @Mapping(target = "fechaCreacion", ignore = true)
  EvolucionLocalizacionLesiones toEntity(EvolucionLocalizacionLesionesRequest request);

  @Mapping(target = "evolucionMedicaId", source = "evolucionMedica.id")
  EvolucionLocalizacionLesionesResponse toResponse(EvolucionLocalizacionLesiones entity);

  List<EvolucionLocalizacionLesionesResponse> toLocalizacionLesionesResponseList(List<EvolucionLocalizacionLesiones> entities);

  // Mappings para EvolucionExamenesSolicitados
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "evolucionMedica", ignore = true)
  @Mapping(target = "fechaCreacion", ignore = true)
  EvolucionExamenesSolicitados toEntity(EvolucionExamenesSolicitadosRequest request);

  @Mapping(target = "evolucionMedicaId", source = "evolucionMedica.id")
  EvolucionExamenesSolicitadosResponse toResponse(EvolucionExamenesSolicitados entity);

  List<EvolucionExamenesSolicitadosResponse> toExamenesSolicitadosResponseList(List<EvolucionExamenesSolicitados> entities);

  // Mappings para EvolucionDiagnosticos
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "evolucionMedica", ignore = true)
  @Mapping(target = "fechaCreacion", ignore = true)
  EvolucionDiagnosticos toEntity(EvolucionDiagnosticosRequest request);

  @Mapping(target = "evolucionMedicaId", source = "evolucionMedica.id")
  EvolucionDiagnosticosResponse toResponse(EvolucionDiagnosticos entity);

  List<EvolucionDiagnosticosResponse> toDiagnosticosResponseList(List<EvolucionDiagnosticos> entities);

  // Mappings para EvolucionPlanesTratamiento
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "evolucionMedica", ignore = true)
  @Mapping(target = "indicacionesMedicas", ignore = true)
  @Mapping(target = "fechaCreacion", ignore = true)
  EvolucionPlanesTratamiento toEntity(EvolucionPlanesTratamientoRequest request);

  @Mapping(target = "evolucionMedicaId", source = "evolucionMedica.id")
  @Mapping(target = "indicacionesMedicas", source = "indicacionesMedicas")
  EvolucionPlanesTratamientoResponse toResponse(EvolucionPlanesTratamiento entity);

  List<EvolucionPlanesTratamientoResponse> toPlanesTratamientoResponseList(List<EvolucionPlanesTratamiento> entities);

  // Mappings para EvolucionIndicacionesMedicas
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "planTratamiento", ignore = true)
  @Mapping(target = "fechaCreacion", ignore = true)
  EvolucionIndicacionesMedicas toEntity(EvolucionIndicacionesMedicasRequest request);

  @Mapping(target = "planTratamientoId", source = "planTratamiento.id")
  EvolucionIndicacionesMedicasResponse toResponse(EvolucionIndicacionesMedicas entity);

  List<EvolucionIndicacionesMedicasResponse> toIndicacionesMedicasResponseList(List<EvolucionIndicacionesMedicas> entities);

  // Mappings para EvolucionEmergenciaObstetrica
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "evolucionMedica", ignore = true)
  @Mapping(target = "fechaCreacion", ignore = true)
  EvolucionEmergenciaObstetrica toEntity(EvolucionEmergenciaObstetricaRequest request);

  @Mapping(target = "evolucionMedicaId", source = "evolucionMedica.id")
  EvolucionEmergenciaObstetricaResponse toResponse(EvolucionEmergenciaObstetrica entity);

  // Mappings para EvolucionAltaMedica
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "evolucionMedica", ignore = true)
  @Mapping(target = "fechaCreacion", ignore = true)
  EvolucionAltaMedica toEntity(EvolucionAltaMedicaRequest request);

  @Mapping(target = "evolucionMedicaId", source = "evolucionMedica.id")
  EvolucionAltaMedicaResponse toResponse(EvolucionAltaMedica entity);
}