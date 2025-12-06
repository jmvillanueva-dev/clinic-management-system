package com.clinic.webapi.modules.evolucionesmedicas.model.mapper;

import com.clinic.webapi.modules.evolucionesmedicas.dto.EvolucionMedicaResumenResponse;
import com.clinic.webapi.modules.evolucionesmedicas.entity.EvolucionMedica;
import com.clinic.webapi.modules.evolucionesmedicas.repository.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class EvolucionResumenMapper {

  @Autowired
  protected EvolucionMotivoAtencionRepository motivoAtencionRepository;
  @Autowired
  protected EvolucionSignosVitalesRepository signosVitalesRepository;
  @Autowired
  protected EvolucionAntecedentesIncidenteRepository antecedentesIncidenteRepository;
  @Autowired
  protected EvolucionValoracionClinicaRepository valoracionClinicaRepository;
  @Autowired
  protected EvolucionDiagnosticosRepository diagnosticosRepository;
  @Autowired
  protected EvolucionPlanesTratamientoRepository planesTratamientoRepository;
  @Autowired
  protected EvolucionExamenesSolicitadosRepository examenesSolicitadosRepository;
  @Autowired
  protected EvolucionLocalizacionLesionesRepository localizacionLesionesRepository;
  @Autowired
  protected EvolucionEmergenciaObstetricaRepository emergenciaObstetricaRepository;
  @Autowired
  protected EvolucionAltaMedicaRepository altaMedicaRepository;


  @Mapping(target = "historiaClinicaId", source = "historiaClinica.id")
  @Mapping(target = "numeroHistoriaClinica", source = "historiaClinica.numeroHistoriaClinica")
  @Mapping(target = "empleadoId", source = "empleado.id")
  @Mapping(target = "empleadoNombreCompleto", expression = "java(getNombreCompletoEmpleado(evolucionMedica))")
  @Mapping(target = "tieneMotivoAtencion", expression = "java(hasMotivoAtencion(evolucionMedica))")
  @Mapping(target = "tieneSignosVitales", expression = "java(hasSignosVitales(evolucionMedica))")
  @Mapping(target = "tieneAntecedentesIncidente", expression = "java(hasAntecedentesIncidente(evolucionMedica))")
  @Mapping(target = "tieneValoracionClinica", expression = "java(hasValoracionClinica(evolucionMedica))")
  @Mapping(target = "tieneDiagnosticos", expression = "java(hasDiagnosticos(evolucionMedica))")
  @Mapping(target = "tienePlanesTratamiento", expression = "java(hasPlanesTratamiento(evolucionMedica))")
  @Mapping(target = "tieneExamenesSolicitados", expression = "java(hasExamenesSolicitados(evolucionMedica))")
  @Mapping(target = "tieneLocalizacionLesiones", expression = "java(hasLocalizacionLesiones(evolucionMedica))")
  @Mapping(target = "tieneEmergenciaObstetrica", expression = "java(hasEmergenciaObstetrica(evolucionMedica))")
  @Mapping(target = "tieneAltaMedica", expression = "java(hasAltaMedica(evolucionMedica))")
  public abstract EvolucionMedicaResumenResponse toResumenResponse(EvolucionMedica evolucionMedica);

  public abstract List<EvolucionMedicaResumenResponse> toResumenResponseList(List<EvolucionMedica> evoluciones);

  protected String getNombreCompletoEmpleado(EvolucionMedica evolucionMedica) {
    if (evolucionMedica.getEmpleado() == null) {
      return "";
    }
    return evolucionMedica.getEmpleado().getNombre() + " " + evolucionMedica.getEmpleado().getApellido();
  }

  protected boolean hasMotivoAtencion(EvolucionMedica evolucionMedica) {
    return motivoAtencionRepository.existsByEvolucionMedicaId(evolucionMedica.getId());
  }

  protected boolean hasSignosVitales(EvolucionMedica evolucionMedica) {
    return signosVitalesRepository.existsByEvolucionMedicaId(evolucionMedica.getId());
  }

  protected boolean hasAntecedentesIncidente(EvolucionMedica evolucionMedica) {
    return antecedentesIncidenteRepository.existsByEvolucionMedicaId(evolucionMedica.getId());
  }

  protected boolean hasValoracionClinica(EvolucionMedica evolucionMedica) {
    return valoracionClinicaRepository.existsByEvolucionMedicaId(evolucionMedica.getId());
  }

  protected boolean hasDiagnosticos(EvolucionMedica evolucionMedica) {
    return !diagnosticosRepository.findByEvolucionMedicaId(evolucionMedica.getId()).isEmpty();
  }

  protected boolean hasPlanesTratamiento(EvolucionMedica evolucionMedica) {
    return !planesTratamientoRepository.findByEvolucionMedicaId(evolucionMedica.getId()).isEmpty();
  }

  protected boolean hasExamenesSolicitados(EvolucionMedica evolucionMedica) {
    return !examenesSolicitadosRepository.findByEvolucionMedicaId(evolucionMedica.getId()).isEmpty();
  }

  protected boolean hasLocalizacionLesiones(EvolucionMedica evolucionMedica) {
    return !localizacionLesionesRepository.findByEvolucionMedicaId(evolucionMedica.getId()).isEmpty();
  }

  protected boolean hasEmergenciaObstetrica(EvolucionMedica evolucionMedica) {
    return emergenciaObstetricaRepository.existsByEvolucionMedicaId(evolucionMedica.getId());
  }

  protected boolean hasAltaMedica(EvolucionMedica evolucionMedica) {
    return altaMedicaRepository.existsByEvolucionMedicaId(evolucionMedica.getId());
  }
}