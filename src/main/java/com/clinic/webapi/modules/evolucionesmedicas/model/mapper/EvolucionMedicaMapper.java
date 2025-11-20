package com.clinic.webapi.modules.evolucionesmedicas.model.mapper;

import com.clinic.webapi.modules.evolucionesmedicas.dto.*;
import com.clinic.webapi.modules.evolucionesmedicas.entity.*;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {EvolucionSeccionesMapper.class})
public interface EvolucionMedicaMapper {

  // Mappings para EvolucionMedica
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "historiaClinica", ignore = true)
  @Mapping(target = "empleado", ignore = true)
  @Mapping(target = "motivoAtencion", ignore = true)
  @Mapping(target = "antecedentesIncidente", ignore = true)
  @Mapping(target = "signosVitales", ignore = true)
  @Mapping(target = "valoracionClinica", ignore = true)
  @Mapping(target = "localizacionLesiones", ignore = true)
  @Mapping(target = "examenesSolicitados", ignore = true)
  @Mapping(target = "diagnosticos", ignore = true)
  @Mapping(target = "planesTratamiento", ignore = true)
  @Mapping(target = "emergenciaObstetrica", ignore = true)
  @Mapping(target = "altaMedica", ignore = true)
  @Mapping(target = "fechaCreacion", ignore = true)
  @Mapping(target = "fechaActualizacion", ignore = true)
  EvolucionMedica toEntity(EvolucionMedicaRequest request);

  @Mapping(target = "historiaClinicaId", source = "historiaClinica.id")
  @Mapping(target = "numeroHistoriaClinica", source = "historiaClinica.numeroHistoriaClinica")
  @Mapping(target = "empleadoId", source = "empleado.id")
  @Mapping(target = "empleadoNombreCompleto", expression = "java(getNombreCompletoEmpleado(evolucionMedica))")
  @Mapping(target = "empleadoEspecialidad", source = "empleado.especialidad")
  @Mapping(target = "motivoAtencion", source = "motivoAtencion")
  @Mapping(target = "antecedentesIncidente", source = "antecedentesIncidente")
  @Mapping(target = "signosVitales", source = "signosVitales")
  @Mapping(target = "valoracionClinica", source = "valoracionClinica")
  @Mapping(target = "localizacionLesiones", source = "localizacionLesiones")
  @Mapping(target = "examenesSolicitados", source = "examenesSolicitados")
  @Mapping(target = "diagnosticos", source = "diagnosticos")
  @Mapping(target = "planesTratamiento", source = "planesTratamiento")
  @Mapping(target = "emergenciaObstetrica", source = "emergenciaObstetrica")
  @Mapping(target = "altaMedica", source = "altaMedica")
  EvolucionMedicaResponse toResponse(EvolucionMedica evolucionMedica);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "historiaClinica", ignore = true)
  @Mapping(target = "empleado", ignore = true)
  @Mapping(target = "motivoAtencion", ignore = true)
  @Mapping(target = "antecedentesIncidente", ignore = true)
  @Mapping(target = "signosVitales", ignore = true)
  @Mapping(target = "valoracionClinica", ignore = true)
  @Mapping(target = "localizacionLesiones", ignore = true)
  @Mapping(target = "examenesSolicitados", ignore = true)
  @Mapping(target = "diagnosticos", ignore = true)
  @Mapping(target = "planesTratamiento", ignore = true)
  @Mapping(target = "emergenciaObstetrica", ignore = true)
  @Mapping(target = "altaMedica", ignore = true)
  @Mapping(target = "fechaCreacion", ignore = true)
  @Mapping(target = "fechaActualizacion", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateEntityFromRequest(@MappingTarget EvolucionMedica evolucionMedica, EvolucionMedicaUpdateRequest request);

  // Métodos auxiliares
  default String getNombreCompletoEmpleado(EvolucionMedica evolucionMedica) {
    if (evolucionMedica.getEmpleado() == null) {
      return "";
    }
    return evolucionMedica.getEmpleado().getNombre() + " " + evolucionMedica.getEmpleado().getApellido();
  }
}