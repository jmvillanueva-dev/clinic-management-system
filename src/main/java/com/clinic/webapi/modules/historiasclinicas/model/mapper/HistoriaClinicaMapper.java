package com.clinic.webapi.modules.historiasclinicas.model.mapper;

import com.clinic.webapi.modules.historiasclinicas.dto.HistoriaClinicaRequest;
import com.clinic.webapi.modules.historiasclinicas.dto.HistoriaClinicaResponse;
import com.clinic.webapi.modules.historiasclinicas.entity.HistoriaClinica;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface HistoriaClinicaMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "paciente", ignore = true)
  @Mapping(target = "numeroHistoriaClinica", ignore = true)
  @Mapping(target = "fechaCreacion", ignore = true)
  @Mapping(target = "fechaActualizacion", ignore = true)
  @Mapping(target = "evolucionesMedicas", ignore = true)
  HistoriaClinica toEntity(HistoriaClinicaRequest request);

  @Mapping(target = "pacienteId", source = "paciente.id")
  @Mapping(target = "pacienteCedula", source = "paciente.cedula")
  @Mapping(target = "pacienteNombreCompleto", expression = "java(getNombreCompletoPaciente(historiaClinica))")
  @Mapping(target = "totalEvoluciones", ignore = true)
  HistoriaClinicaResponse toResponse(HistoriaClinica historiaClinica);

  default String getNombreCompletoPaciente(HistoriaClinica historiaClinica) {
    if (historiaClinica.getPaciente() == null) {
      return "";
    }

    String nombreCompleto = historiaClinica.getPaciente().getPrimerNombre();

    if (historiaClinica.getPaciente().getSegundoNombre() != null) {
      nombreCompleto += " " + historiaClinica.getPaciente().getSegundoNombre();
    }

    nombreCompleto += " " + historiaClinica.getPaciente().getApellidoPaterno();

    if (historiaClinica.getPaciente().getApellidoMaterno() != null) {
      nombreCompleto += " " + historiaClinica.getPaciente().getApellidoMaterno();
    }

    return nombreCompleto;
  }

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "paciente", ignore = true)
  @Mapping(target = "numeroHistoriaClinica", ignore = true)
  @Mapping(target = "fechaCreacion", ignore = true)
  @Mapping(target = "fechaActualizacion", ignore = true)
  @Mapping(target = "evolucionesMedicas", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  HistoriaClinica updateEntityFromRequest(@MappingTarget HistoriaClinica historiaClinica, HistoriaClinicaRequest request);
}