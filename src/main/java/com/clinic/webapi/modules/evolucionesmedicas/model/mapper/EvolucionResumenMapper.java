package com.clinic.webapi.modules.evolucionesmedicas.model.mapper;

import com.clinic.webapi.modules.evolucionesmedicas.dto.EvolucionMedicaResumenResponse;
import com.clinic.webapi.modules.evolucionesmedicas.entity.EvolucionMedica;
import com.clinic.webapi.modules.evolucionesmedicas.repository.EvolucionDiagnosticosRepository;
import com.clinic.webapi.modules.evolucionesmedicas.repository.EvolucionPlanesTratamientoRepository;
import com.clinic.webapi.modules.evolucionesmedicas.repository.EvolucionSignosVitalesRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class EvolucionResumenMapper {

  @Autowired
  protected EvolucionSignosVitalesRepository signosVitalesRepository;

  @Autowired
  protected EvolucionDiagnosticosRepository diagnosticosRepository;

  @Autowired
  protected EvolucionPlanesTratamientoRepository planesTratamientoRepository;

  @Mapping(target = "historiaClinicaId", source = "historiaClinica.id")
  @Mapping(target = "numeroHistoriaClinica", source = "historiaClinica.numeroHistoriaClinica")
  @Mapping(target = "empleadoId", source = "empleado.id")
  @Mapping(target = "empleadoNombreCompleto", expression = "java(getNombreCompletoEmpleado(evolucionMedica))")
  @Mapping(target = "tieneSignosVitales", expression = "java(hasSignosVitales(evolucionMedica))")
  @Mapping(target = "tieneDiagnosticos", expression = "java(hasDiagnosticos(evolucionMedica))")
  @Mapping(target = "tieneTratamientos", expression = "java(hasTratamientos(evolucionMedica))")
  public abstract EvolucionMedicaResumenResponse toResumenResponse(EvolucionMedica evolucionMedica);

  public abstract List<EvolucionMedicaResumenResponse> toResumenResponseList(List<EvolucionMedica> evoluciones);

  protected String getNombreCompletoEmpleado(EvolucionMedica evolucionMedica) {
    if (evolucionMedica.getEmpleado() == null) {
      return "";
    }
    return evolucionMedica.getEmpleado().getNombre() + " " + evolucionMedica.getEmpleado().getApellido();
  }

  protected boolean hasSignosVitales(EvolucionMedica evolucionMedica) {
    return signosVitalesRepository.existsByEvolucionMedicaId(evolucionMedica.getId());
  }

  protected boolean hasDiagnosticos(EvolucionMedica evolucionMedica) {
    return !diagnosticosRepository.findByEvolucionMedicaId(evolucionMedica.getId()).isEmpty();
  }

  protected boolean hasTratamientos(EvolucionMedica evolucionMedica) {
    return !planesTratamientoRepository.findByEvolucionMedicaId(evolucionMedica.getId()).isEmpty();
  }
}