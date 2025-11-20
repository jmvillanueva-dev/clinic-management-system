package com.clinic.webapi.modules.evolucionesmedicas.model.mapper;

import com.clinic.webapi.modules.evolucionesmedicas.dto.*;
import com.clinic.webapi.modules.evolucionesmedicas.entity.*;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface EvolucionUpdateMapper {

  // Update mappings para secciones individuales
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "evolucionMedica", ignore = true)
  @Mapping(target = "fechaCreacion", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateMotivoAtencionFromRequest(@MappingTarget EvolucionMotivoAtencion entity, EvolucionMotivoAtencionRequest request);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "evolucionMedica", ignore = true)
  @Mapping(target = "fechaCreacion", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateAntecedentesIncidenteFromRequest(@MappingTarget EvolucionAntecedentesIncidente entity, EvolucionAntecedentesIncidenteRequest request);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "evolucionMedica", ignore = true)
  @Mapping(target = "imc", ignore = true) // Se recalcula en el servicio
  @Mapping(target = "fechaCreacion", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateSignosVitalesFromRequest(@MappingTarget EvolucionSignosVitales entity, EvolucionSignosVitalesRequest request);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "evolucionMedica", ignore = true)
  @Mapping(target = "fechaCreacion", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateValoracionClinicaFromRequest(@MappingTarget EvolucionValoracionClinica entity, EvolucionValoracionClinicaRequest request);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "evolucionMedica", ignore = true)
  @Mapping(target = "fechaCreacion", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateEmergenciaObstetricaFromRequest(@MappingTarget EvolucionEmergenciaObstetrica entity, EvolucionEmergenciaObstetricaRequest request);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "evolucionMedica", ignore = true)
  @Mapping(target = "fechaCreacion", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateAltaMedicaFromRequest(@MappingTarget EvolucionAltaMedica entity, EvolucionAltaMedicaRequest request);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "evolucionMedica", ignore = true)
  @Mapping(target = "fechaCreacion", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateDiagnosticoFromRequest(@MappingTarget EvolucionDiagnosticos entity, EvolucionDiagnosticosRequest request);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "evolucionMedica", ignore = true)
  @Mapping(target = "fechaCreacion", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateExamenSolicitadoFromRequest(@MappingTarget EvolucionExamenesSolicitados entity, EvolucionExamenesSolicitadosRequest request);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "evolucionMedica", ignore = true)
  @Mapping(target = "fechaCreacion", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateLocalizacionLesionFromRequest(@MappingTarget EvolucionLocalizacionLesiones entity, EvolucionLocalizacionLesionesRequest request);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "evolucionMedica", ignore = true)
  @Mapping(target = "indicacionesMedicas", ignore = true)
  @Mapping(target = "fechaCreacion", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updatePlanTratamientoFromRequest(@MappingTarget EvolucionPlanesTratamiento entity, EvolucionPlanesTratamientoRequest request);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "planTratamiento", ignore = true)
  @Mapping(target = "fechaCreacion", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateIndicacionMedicaFromRequest(@MappingTarget EvolucionIndicacionesMedicas entity, EvolucionIndicacionesMedicasRequest request);
}