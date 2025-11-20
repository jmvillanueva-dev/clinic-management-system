package com.clinic.webapi.modules.pacientes.model.mapper;

import com.clinic.webapi.modules.catalogos.dto.ItemCatalogoResponse;
import com.clinic.webapi.modules.catalogos.model.entity.ItemCatalogo;
import com.clinic.webapi.modules.pacientes.dto.*;
import com.clinic.webapi.modules.pacientes.model.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PacienteMapper {

    // Solo para mapeo básico, no incluye relaciones
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "fechaActualizacion", ignore = true)
    @Mapping(target = "grupoSanguineo", ignore = true)
    @Mapping(target = "ocupacion", ignore = true)
    @Mapping(target = "datosDemograficos", ignore = true)
    @Mapping(target = "ubicacionGeografica", ignore = true)
    @Mapping(target = "fuenteInformacion", ignore = true)
    @Mapping(target = "contactosEmergencia", ignore = true)
    @Mapping(target = "antecedentesClinicos", ignore = true)
    Paciente toEntity(PacienteRequest request);

    @Mapping(target = "fechaNacimiento", source = "datosDemograficos.fechaNacimiento")
    @Mapping(target = "lugarNacimiento", source = "datosDemograficos.lugarNacimiento")
    @Mapping(target = "genero", source = "datosDemograficos.genero")
    @Mapping(target = "nacionalidad", source = "datosDemograficos.nacionalidad")
    @Mapping(target = "grupoCultural", source = "datosDemograficos.grupoCultural")
    @Mapping(target = "estadoCivil", source = "datosDemograficos.estadoCivil")
    @Mapping(target = "nivelInstruccion", source = "datosDemograficos.nivelInstruccion")
    @Mapping(target = "direccion", source = "ubicacionGeografica.direccion")
    @Mapping(target = "provincia", source = "ubicacionGeografica.provincia")
    @Mapping(target = "canton", source = "ubicacionGeografica.canton")
    @Mapping(target = "parroquia", source = "ubicacionGeografica.parroquia")
    @Mapping(target = "ocupacion", source = "ocupacion")
    @Mapping(target = "fuenteInformacion", source = "fuenteInformacion")
    @Mapping(target = "contactosEmergencia", source = "contactosEmergencia")
    @Mapping(target = "antecedentesClinicos", source = "antecedentesClinicos")
    PacienteResponse toResponse(Paciente paciente);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    Paciente updateEntityFromRequest(@MappingTarget Paciente paciente, PacienteRequest request);

    // Mappers para entidades relacionadas
    default OcupacionResponse mapOcupacion(PacienteOcupacion ocupacion) {
        if (ocupacion == null) {
            return null;
        }
        return OcupacionResponse.builder()
                .id(ocupacion.getId())
                .ocupacion(mapItemCatalogo(ocupacion.getOcupacion()))
                .nombreEmpresa(ocupacion.getNombreEmpresa())
                .cargo(ocupacion.getCargo())
                .telefonoEmpresa(ocupacion.getTelefonoEmpresa())
                .direccionEmpresa(ocupacion.getDireccionEmpresa())
                .fechaInicio(ocupacion.getFechaInicio())
                .fechaFin(ocupacion.getFechaFin())
                .actual(ocupacion.isActual())
                .fechaCreacion(ocupacion.getFechaCreacion())
                .fechaActualizacion(ocupacion.getFechaActualizacion())
                .build();
    }

    default FuenteInformacionResponse mapFuenteInformacion(PacienteFuenteInformacion fuenteInformacion) {
        if (fuenteInformacion == null) {
            return null;
        }
        return FuenteInformacionResponse.builder()
                .id(fuenteInformacion.getId())
                .fuenteInformacion(mapItemCatalogo(fuenteInformacion.getFuenteInformacion()))
                .nombreFuenteInfo(fuenteInformacion.getNombreFuenteInfo())
                .telefono(fuenteInformacion.getTelefono())
                .observaciones(fuenteInformacion.getObservaciones())
                .fechaCreacion(fuenteInformacion.getFechaCreacion())
                .fechaActualizacion(fuenteInformacion.getFechaActualizacion())
                .build();
    }

    default List<ContactoEmergenciaResponse> mapContactosEmergencia(List<PacienteContactoEmergencia> contactos) {
        if (contactos == null) {
            return null;
        }
        return contactos.stream()
                .map(this::mapContactoEmergencia)
                .collect(Collectors.toList());
    }

    default ContactoEmergenciaResponse mapContactoEmergencia(PacienteContactoEmergencia contacto) {
        if (contacto == null) {
            return null;
        }
        return ContactoEmergenciaResponse.builder()
                .id(contacto.getId())
                .pacienteId(contacto.getPaciente().getId())
                .nombre(contacto.getNombre())
                .parentescoId(contacto.getParentesco().getId())
                .parentescoNombre(contacto.getParentesco().getNombre())
                .telefono(contacto.getTelefono())
                .direccion(contacto.getDireccion())
                .build();
    }

    default List<AntecedenteClinicoResponse> mapAntecedentesClinicos(List<PacienteAntecedenteClinico> antecedentes) {
        if (antecedentes == null) {
            return null;
        }
        return antecedentes.stream()
                .map(this::mapAntecedenteClinico)
                .collect(Collectors.toList());
    }

    default AntecedenteClinicoResponse mapAntecedenteClinico(PacienteAntecedenteClinico antecedente) {
        if (antecedente == null) {
            return null;
        }
        return AntecedenteClinicoResponse.builder()
                .id(antecedente.getId())
                .pacienteId(antecedente.getPaciente().getId())
                .tipoAntecedenteId(antecedente.getTipoAntecedente().getId())
                .tipoAntecedenteNombre(antecedente.getTipoAntecedente().getNombre())
                .patologiaId(antecedente.getPatologia().getId())
                .patologiaNombre(antecedente.getPatologia().getNombre())
                .descripcion(antecedente.getDescripcion())
                .fechaDiagnostico(antecedente.getFechaDiagnostico())
                .tratamiento(antecedente.getTratamiento())
                .estaActivo(antecedente.isEstaActivo())
                .fechaCreacion(antecedente.getFechaCreacion())
                .build();
    }

    default ItemCatalogoResponse mapItemCatalogo(ItemCatalogo item) {
        if (item == null) {
            return null;
        }
        return ItemCatalogoResponse.builder()
                .id(item.getId())
                .catalogoId(item.getCatalogo() != null ? item.getCatalogo().getId() : null)
                .catalogoNombre(item.getCatalogo() != null ? item.getCatalogo().getNombre() : null)
                .nombre(item.getNombre())
                .descripcion(item.getDescripcion())
                .codigo(item.getCodigo())
                .valor(item.getValor())
                .estaActivo(item.isEstaActivo())
                .fechaCreacion(item.getFechaCreacion())
                .fechaActualizacion(item.getFechaActualizacion())
                .build();
    }
}
