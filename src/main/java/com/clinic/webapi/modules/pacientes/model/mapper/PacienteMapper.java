package com.clinic.webapi.modules.pacientes.model.mapper;

import com.clinic.webapi.modules.pacientes.dto.PacienteRequest;
import com.clinic.webapi.modules.pacientes.dto.PacienteResponse;
import com.clinic.webapi.modules.pacientes.model.entity.Paciente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PacienteMapper {

  Paciente toEntity(PacienteRequest request);

  PacienteResponse toResponse(Paciente paciente);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "fechaCreacion", ignore = true)
  Paciente updateEntityFromRequest(@MappingTarget Paciente paciente, PacienteRequest request);
}