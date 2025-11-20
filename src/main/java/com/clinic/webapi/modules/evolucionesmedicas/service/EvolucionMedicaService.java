package com.clinic.webapi.modules.evolucionesmedicas.service;

import com.clinic.webapi.modules.empleados.entity.Empleado;
import com.clinic.webapi.modules.empleados.repository.EmpleadoRepository;
import com.clinic.webapi.modules.evolucionesmedicas.dto.*;
import com.clinic.webapi.modules.evolucionesmedicas.entity.*;
import com.clinic.webapi.modules.evolucionesmedicas.model.mapper.EvolucionMedicaMapper;
import com.clinic.webapi.modules.evolucionesmedicas.repository.*;
import com.clinic.webapi.modules.historiasclinicas.entity.HistoriaClinica;
import com.clinic.webapi.modules.historiasclinicas.repository.HistoriaClinicaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EvolucionMedicaService {

  private final EvolucionMedicaRepository evolucionMedicaRepository;
  private final HistoriaClinicaRepository historiaClinicaRepository;
  private final EmpleadoRepository empleadoRepository;
  private final EvolucionMedicaMapper evolucionMedicaMapper;

  // Repositories para las secciones
  private final EvolucionMotivoAtencionRepository motivoAtencionRepository;
  private final EvolucionAntecedentesIncidenteRepository antecedentesIncidenteRepository;
  private final EvolucionSignosVitalesRepository signosVitalesRepository;
  private final EvolucionValoracionClinicaRepository valoracionClinicaRepository;
  private final EvolucionLocalizacionLesionesRepository localizacionLesionesRepository;
  private final EvolucionExamenesSolicitadosRepository examenesSolicitadosRepository;
  private final EvolucionDiagnosticosRepository diagnosticosRepository;
  private final EvolucionPlanesTratamientoRepository planesTratamientoRepository;
  private final EvolucionIndicacionesMedicasRepository indicacionesMedicasRepository;
  private final EvolucionEmergenciaObstetricaRepository emergenciaObstetricaRepository;
  private final EvolucionAltaMedicaRepository altaMedicaRepository;

  @Transactional
  public EvolucionMedicaResponse crearEvolucionMedica(EvolucionMedicaRequest request) {
    // Validar historia clínica
    HistoriaClinica historiaClinica = historiaClinicaRepository.findById(request.getHistoriaClinicaId())
        .orElseThrow(() -> new RuntimeException("Historia clínica no encontrada con ID: " + request.getHistoriaClinicaId()));

    // Validar empleado (médico)
    Empleado empleado = empleadoRepository.findById(request.getEmpleadoId())
        .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + request.getEmpleadoId()));

    // Crear evolución médica base
    EvolucionMedica evolucionMedica = EvolucionMedica.builder()
        .historiaClinica(historiaClinica)
        .empleado(empleado)
        .fechaConsulta(request.getFechaConsulta() != null ? request.getFechaConsulta() : Instant.now())
        .tipoConsulta(request.getTipoConsulta())
        .estado(request.getEstado() != null ? request.getEstado() : "ACTIVA")
        .observacionesGenerales(request.getObservacionesGenerales())
        .build();

    EvolucionMedica evolucionGuardada = evolucionMedicaRepository.save(evolucionMedica);

    // Procesar secciones opcionales
    procesarSecciones(evolucionGuardada, request);

    return obtenerEvolucionMedicaCompleta(evolucionGuardada.getId());
  }

  private void procesarSecciones(EvolucionMedica evolucionMedica, EvolucionMedicaRequest request) {
    // 1. Motivo de atención
    if (request.getMotivoAtencion() != null) {
      crearMotivoAtencion(evolucionMedica, request.getMotivoAtencion());
    }

    // 2. Antecedentes del incidente
    if (request.getAntecedentesIncidente() != null) {
      crearAntecedentesIncidente(evolucionMedica, request.getAntecedentesIncidente());
    }

    // 3. Signos vitales (con cálculo automático de IMC)
    if (request.getSignosVitales() != null) {
      crearSignosVitales(evolucionMedica, request.getSignosVitales());
    }

    // 4. Valoración clínica
    if (request.getValoracionClinica() != null) {
      crearValoracionClinica(evolucionMedica, request.getValoracionClinica());
    }

    // 5. Localización de lesiones
    if (request.getLocalizacionLesiones() != null && !request.getLocalizacionLesiones().isEmpty()) {
      crearLocalizacionLesiones(evolucionMedica, request.getLocalizacionLesiones());
    }

    // 6. Exámenes solicitados
    if (request.getExamenesSolicitados() != null && !request.getExamenesSolicitados().isEmpty()) {
      crearExamenesSolicitados(evolucionMedica, request.getExamenesSolicitados());
    }

    // 7. Diagnósticos
    if (request.getDiagnosticos() != null && !request.getDiagnosticos().isEmpty()) {
      crearDiagnosticos(evolucionMedica, request.getDiagnosticos());
    }

    // 8. Planes de tratamiento con indicaciones
    if (request.getPlanesTratamiento() != null && !request.getPlanesTratamiento().isEmpty()) {
      crearPlanesTratamiento(evolucionMedica, request.getPlanesTratamiento());
    }

    // 9. Emergencia obstétrica
    if (request.getEmergenciaObstetrica() != null) {
      crearEmergenciaObstetrica(evolucionMedica, request.getEmergenciaObstetrica());
    }

    // 10. Alta médica
    if (request.getAltaMedica() != null) {
      crearAltaMedica(evolucionMedica, request.getAltaMedica());
    }
  }

  // Métodos auxiliares para crear cada sección
  private void crearMotivoAtencion(EvolucionMedica evolucionMedica, EvolucionMotivoAtencionRequest request) {
    EvolucionMotivoAtencion motivoAtencion = EvolucionMotivoAtencion.builder()
        .evolucionMedica(evolucionMedica)
        .motivoConsulta(request.getMotivoConsulta())
        .enfermedadActual(request.getEnfermedadActual())
        .build();
    motivoAtencionRepository.save(motivoAtencion);
  }

  private void crearAntecedentesIncidente(EvolucionMedica evolucionMedica, EvolucionAntecedentesIncidenteRequest request) {
    EvolucionAntecedentesIncidente antecedentes = EvolucionAntecedentesIncidente.builder()
        .evolucionMedica(evolucionMedica)
        .antecedentesPersonales(request.getAntecedentesPersonales())
        .antecedentesFamiliares(request.getAntecedentesFamiliares())
        .habitosToxicos(request.getHabitosToxicos())
        .alergias(request.getAlergias())
        .medicamentosActuales(request.getMedicamentosActuales())
        .build();
    antecedentesIncidenteRepository.save(antecedentes);
  }

  private void crearSignosVitales(EvolucionMedica evolucionMedica, EvolucionSignosVitalesRequest request) {
    // Calcular IMC automáticamente si hay peso y talla
    BigDecimal imc = null;
    if (request.getPeso() != null && request.getTalla() != null && request.getTalla().compareTo(BigDecimal.ZERO) > 0) {
      // IMC = peso (kg) / (talla (m) * talla (m))
      BigDecimal tallaMetros = request.getTalla().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
      imc = request.getPeso().divide(tallaMetros.pow(2), 2, RoundingMode.HALF_UP);
    }

    EvolucionSignosVitales signosVitales = EvolucionSignosVitales.builder()
        .evolucionMedica(evolucionMedica)
        .presionArterialSistolica(request.getPresionArterialSistolica())
        .presionArterialDiastolica(request.getPresionArterialDiastolica())
        .frecuenciaCardiaca(request.getFrecuenciaCardiaca())
        .frecuenciaRespiratoria(request.getFrecuenciaRespiratoria())
        .temperatura(request.getTemperatura())
        .saturacionOxigeno(request.getSaturacionOxigeno())
        .peso(request.getPeso())
        .talla(request.getTalla())
        .imc(imc)
        .glucosa(request.getGlucosa())
        .build();
    signosVitalesRepository.save(signosVitales);
  }

  private void crearValoracionClinica(EvolucionMedica evolucionMedica, EvolucionValoracionClinicaRequest request) {
    EvolucionValoracionClinica valoracionClinica = EvolucionValoracionClinica.builder()
        .evolucionMedica(evolucionMedica)
        .inspeccionGeneral(request.getInspeccionGeneral())
        .cabezaCuello(request.getCabezaCuello())
        .torax(request.getTorax())
        .abdomen(request.getAbdomen())
        .extremidades(request.getExtremidades())
        .neurologico(request.getNeurologico())
        .pielTegumentos(request.getPielTegumentos())
        .otrosHallazgos(request.getOtrosHallazgos())
        .build();
    valoracionClinicaRepository.save(valoracionClinica);
  }

  private void crearLocalizacionLesiones(EvolucionMedica evolucionMedica, List<EvolucionLocalizacionLesionesRequest> requests) {
    List<EvolucionLocalizacionLesiones> lesiones = requests.stream()
        .map(request -> EvolucionLocalizacionLesiones.builder()
            .evolucionMedica(evolucionMedica)
            .localizacion(request.getLocalizacion())
            .tipoLesion(request.getTipoLesion())
            .descripcion(request.getDescripcion())
            .gravedad(request.getGravedad())
            .build())
        .collect(Collectors.toList());
    localizacionLesionesRepository.saveAll(lesiones);
  }

  private void crearExamenesSolicitados(EvolucionMedica evolucionMedica, List<EvolucionExamenesSolicitadosRequest> requests) {
    List<EvolucionExamenesSolicitados> examenes = requests.stream()
        .map(request -> EvolucionExamenesSolicitados.builder()
            .evolucionMedica(evolucionMedica)
            .tipoExamen(request.getTipoExamen())
            .nombreExamen(request.getNombreExamen())
            .indicaciones(request.getIndicaciones())
            .urgencia(request.getUrgencia() != null ? request.getUrgencia() : "ROUTINA")
            .estado(request.getEstado() != null ? request.getEstado() : "SOLICITADO")
            .build())
        .collect(Collectors.toList());
    examenesSolicitadosRepository.saveAll(examenes);
  }

  private void crearDiagnosticos(EvolucionMedica evolucionMedica, List<EvolucionDiagnosticosRequest> requests) {
    List<EvolucionDiagnosticos> diagnosticos = requests.stream()
        .map(request -> EvolucionDiagnosticos.builder()
            .evolucionMedica(evolucionMedica)
            .codigoCie(request.getCodigoCie())
            .diagnostico(request.getDiagnostico())
            .tipo(request.getTipo())
            .observaciones(request.getObservaciones())
            .build())
        .collect(Collectors.toList());
    diagnosticosRepository.saveAll(diagnosticos);
  }

  private void crearPlanesTratamiento(EvolucionMedica evolucionMedica, List<EvolucionPlanesTratamientoRequest> requests) {
    for (EvolucionPlanesTratamientoRequest planRequest : requests) {
      EvolucionPlanesTratamiento plan = EvolucionPlanesTratamiento.builder()
          .evolucionMedica(evolucionMedica)
          .nombreTratamiento(planRequest.getNombreTratamiento())
          .descripcion(planRequest.getDescripcion())
          .tipoTratamiento(planRequest.getTipoTratamiento())
          .duracion(planRequest.getDuracion())
          .build();

      EvolucionPlanesTratamiento planGuardado = planesTratamientoRepository.save(plan);

      // Crear indicaciones médicas si existen
      if (planRequest.getIndicacionesMedicas() != null && !planRequest.getIndicacionesMedicas().isEmpty()) {
        crearIndicacionesMedicas(planGuardado, planRequest.getIndicacionesMedicas());
      }
    }
  }

  private void crearIndicacionesMedicas(EvolucionPlanesTratamiento planTratamiento, List<EvolucionIndicacionesMedicasRequest> requests) {
    List<EvolucionIndicacionesMedicas> indicaciones = requests.stream()
        .map(request -> EvolucionIndicacionesMedicas.builder()
            .planTratamiento(planTratamiento)
            .medicamento(request.getMedicamento())
            .dosis(request.getDosis())
            .frecuencia(request.getFrecuencia())
            .viaAdministracion(request.getViaAdministracion())
            .duracion(request.getDuracion())
            .indicacionesEspeciales(request.getIndicacionesEspeciales())
            .build())
        .collect(Collectors.toList());
    indicacionesMedicasRepository.saveAll(indicaciones);
  }

  private void crearEmergenciaObstetrica(EvolucionMedica evolucionMedica, EvolucionEmergenciaObstetricaRequest request) {
    EvolucionEmergenciaObstetrica emergenciaObstetrica = EvolucionEmergenciaObstetrica.builder()
        .evolucionMedica(evolucionMedica)
        .gestasPrevias(request.getGestasPrevias())
        .partosPrevios(request.getPartosPrevios())
        .abortosPrevios(request.getAbortosPrevios())
        .fum(request.getFum())
        .fpp(request.getFpp())
        .semanasGestacion(request.getSemanasGestacion())
        .presentacion(request.getPresentacion())
        .dinamicaUterina(request.getDinamicaUterina())
        .latidosFetales(request.getLatidosFetales())
        .observaciones(request.getObservaciones())
        .build();
    emergenciaObstetricaRepository.save(emergenciaObstetrica);
  }

  private void crearAltaMedica(EvolucionMedica evolucionMedica, EvolucionAltaMedicaRequest request) {
    EvolucionAltaMedica altaMedica = EvolucionAltaMedica.builder()
        .evolucionMedica(evolucionMedica)
        .fechaAlta(request.getFechaAlta() != null ? request.getFechaAlta() : Instant.now())
        .tipoAlta(request.getTipoAlta())
        .condicionAlta(request.getCondicionAlta())
        .recomendaciones(request.getRecomendaciones())
        .controlProgramado(request.getControlProgramado())
        .especialidadControl(request.getEspecialidadControl())
        .build();
    altaMedicaRepository.save(altaMedica);
  }

  @Transactional(readOnly = true)
  public EvolucionMedicaResponse obtenerEvolucionMedicaCompleta(UUID id) {
    EvolucionMedica evolucionMedica = evolucionMedicaRepository.findByIdWithAllSections(id)
        .orElseThrow(() -> new RuntimeException("Evolución médica no encontrada con ID: " + id));

    return construirResponseCompleto(evolucionMedica);
  }

  @Transactional(readOnly = true)
  public List<EvolucionMedicaResumenResponse> obtenerEvolucionesPorHistoriaClinica(UUID historiaClinicaId) {
    List<EvolucionMedica> evoluciones = evolucionMedicaRepository.findByHistoriaClinicaIdOrderByFechaConsultaDesc(historiaClinicaId);

    return evoluciones.stream()
        .map(this::construirResumenResponse)
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<EvolucionMedicaResumenResponse> obtenerEvolucionesPorEmpleado(UUID empleadoId) {
    List<EvolucionMedica> evoluciones = evolucionMedicaRepository.findByEmpleadoIdOrderByFechaConsultaDesc(empleadoId);

    return evoluciones.stream()
        .map(this::construirResumenResponse)
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<EvolucionMedicaResumenResponse> obtenerEvolucionesPorFiltros(UUID historiaClinicaId, UUID empleadoId,
                                                                           String estado, Instant fechaInicio, Instant fechaFin) {
    List<EvolucionMedica> evoluciones = evolucionMedicaRepository.findByFiltros(
        historiaClinicaId, empleadoId, estado, fechaInicio, fechaFin);

    return evoluciones.stream()
        .map(this::construirResumenResponse)
        .collect(Collectors.toList());
  }

  @Transactional
  public EvolucionMedicaResponse actualizarEvolucionMedica(UUID id, EvolucionMedicaUpdateRequest request) {
    EvolucionMedica evolucionExistente = evolucionMedicaRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Evolución médica no encontrada con ID: " + id));

    // Actualizar campos básicos
    if (request.getFechaConsulta() != null) {
      evolucionExistente.setFechaConsulta(request.getFechaConsulta());
    }
    if (request.getTipoConsulta() != null) {
      evolucionExistente.setTipoConsulta(request.getTipoConsulta());
    }
    if (request.getEstado() != null) {
      evolucionExistente.setEstado(request.getEstado());
    }
    if (request.getObservacionesGenerales() != null) {
      evolucionExistente.setObservacionesGenerales(request.getObservacionesGenerales());
    }

    EvolucionMedica evolucionActualizada = evolucionMedicaRepository.save(evolucionExistente);

    // Actualizar secciones si se proporcionan
    actualizarSecciones(evolucionActualizada, request);

    return obtenerEvolucionMedicaCompleta(id);
  }

  private void actualizarSecciones(EvolucionMedica evolucionMedica, EvolucionMedicaUpdateRequest request) {
    // Para cada sección, si se proporciona en el request, actualizar o crear
    if (request.getMotivoAtencion() != null) {
      actualizarMotivoAtencion(evolucionMedica, request.getMotivoAtencion());
    }
    if (request.getAntecedentesIncidente() != null) {
      actualizarAntecedentesIncidente(evolucionMedica, request.getAntecedentesIncidente());
    }
    if (request.getSignosVitales() != null) {
      actualizarSignosVitales(evolucionMedica, request.getSignosVitales());
    }
    if (request.getValoracionClinica() != null) {
      actualizarValoracionClinica(evolucionMedica, request.getValoracionClinica());
    }
    if (request.getLocalizacionLesiones() != null) {
      actualizarLocalizacionLesiones(evolucionMedica, request.getLocalizacionLesiones());
    }
    if (request.getExamenesSolicitados() != null) {
      actualizarExamenesSolicitados(evolucionMedica, request.getExamenesSolicitados());
    }
    if (request.getDiagnosticos() != null) {
      actualizarDiagnosticos(evolucionMedica, request.getDiagnosticos());
    }
    if (request.getPlanesTratamiento() != null) {
      actualizarPlanesTratamiento(evolucionMedica, request.getPlanesTratamiento());
    }
    if (request.getEmergenciaObstetrica() != null) {
      actualizarEmergenciaObstetrica(evolucionMedica, request.getEmergenciaObstetrica());
    }
    if (request.getAltaMedica() != null) {
      actualizarAltaMedica(evolucionMedica, request.getAltaMedica());
    }
  }

  // Métodos auxiliares para actualizar cada sección (implementación similar a los métodos de creación)
  private void actualizarMotivoAtencion(EvolucionMedica evolucionMedica, EvolucionMotivoAtencionRequest request) {
    EvolucionMotivoAtencion motivoExistente = motivoAtencionRepository.findByEvolucionMedicaId(evolucionMedica.getId())
        .orElse(EvolucionMotivoAtencion.builder().evolucionMedica(evolucionMedica).build());

    motivoExistente.setMotivoConsulta(request.getMotivoConsulta());
    motivoExistente.setEnfermedadActual(request.getEnfermedadActual());

    motivoAtencionRepository.save(motivoExistente);
  }

  private void actualizarAntecedentesIncidente(EvolucionMedica evolucionMedica, EvolucionAntecedentesIncidenteRequest request) {
    EvolucionAntecedentesIncidente antecedentesExistente = antecedentesIncidenteRepository.findByEvolucionMedicaId(evolucionMedica.getId())
        .orElse(EvolucionAntecedentesIncidente.builder().evolucionMedica(evolucionMedica).build());

    antecedentesExistente.setAntecedentesPersonales(request.getAntecedentesPersonales());
    antecedentesExistente.setAntecedentesFamiliares(request.getAntecedentesFamiliares());
    antecedentesExistente.setHabitosToxicos(request.getHabitosToxicos());
    antecedentesExistente.setAlergias(request.getAlergias());
    antecedentesExistente.setMedicamentosActuales(request.getMedicamentosActuales());

    antecedentesIncidenteRepository.save(antecedentesExistente);
  }

  private void actualizarSignosVitales(EvolucionMedica evolucionMedica, EvolucionSignosVitalesRequest request) {
    // Similar a crearSignosVitales pero buscando el existente primero
    EvolucionSignosVitales signosExistente = signosVitalesRepository.findByEvolucionMedicaId(evolucionMedica.getId())
        .orElse(EvolucionSignosVitales.builder().evolucionMedica(evolucionMedica).build());

    // Calcular IMC automáticamente
    BigDecimal imc = null;
    if (request.getPeso() != null && request.getTalla() != null && request.getTalla().compareTo(BigDecimal.ZERO) > 0) {
      BigDecimal tallaMetros = request.getTalla().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
      imc = request.getPeso().divide(tallaMetros.pow(2), 2, RoundingMode.HALF_UP);
    }

    signosExistente.setPresionArterialSistolica(request.getPresionArterialSistolica());
    signosExistente.setPresionArterialDiastolica(request.getPresionArterialDiastolica());
    signosExistente.setFrecuenciaCardiaca(request.getFrecuenciaCardiaca());
    signosExistente.setFrecuenciaRespiratoria(request.getFrecuenciaRespiratoria());
    signosExistente.setTemperatura(request.getTemperatura());
    signosExistente.setSaturacionOxigeno(request.getSaturacionOxigeno());
    signosExistente.setPeso(request.getPeso());
    signosExistente.setTalla(request.getTalla());
    signosExistente.setImc(imc);
    signosExistente.setGlucosa(request.getGlucosa());

    signosVitalesRepository.save(signosExistente);
  }

  // Implementar métodos similares para las demás secciones...
  private void actualizarValoracionClinica(EvolucionMedica evolucionMedica, EvolucionValoracionClinicaRequest request) {
    // Implementación similar...
  }

  private void actualizarLocalizacionLesiones(EvolucionMedica evolucionMedica, List<EvolucionLocalizacionLesionesRequest> requests) {
    // Eliminar existentes y crear nuevos
    localizacionLesionesRepository.deleteByEvolucionMedicaId(evolucionMedica.getId());
    if (!requests.isEmpty()) {
      crearLocalizacionLesiones(evolucionMedica, requests);
    }
  }

  private void actualizarExamenesSolicitados(EvolucionMedica evolucionMedica, List<EvolucionExamenesSolicitadosRequest> requests) {
    // Implementación similar...
  }

  private void actualizarDiagnosticos(EvolucionMedica evolucionMedica, List<EvolucionDiagnosticosRequest> requests) {
    // Implementación similar...
  }

  private void actualizarPlanesTratamiento(EvolucionMedica evolucionMedica, List<EvolucionPlanesTratamientoRequest> requests) {
    // Implementación similar pero más compleja por las indicaciones anidadas...
  }

  private void actualizarEmergenciaObstetrica(EvolucionMedica evolucionMedica, EvolucionEmergenciaObstetricaRequest request) {
    // Implementación similar...
  }

  private void actualizarAltaMedica(EvolucionMedica evolucionMedica, EvolucionAltaMedicaRequest request) {
    // Implementación similar...
  }

  @Transactional
  public void eliminarEvolucionMedica(UUID id) {
    EvolucionMedica evolucionMedica = evolucionMedicaRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Evolución médica no encontrada con ID: " + id));

    evolucionMedicaRepository.delete(evolucionMedica);
  }

  @Transactional(readOnly = true)
  public Long contarEvolucionesPorHistoriaClinica(UUID historiaClinicaId) {
    return evolucionMedicaRepository.countByHistoriaClinicaId(historiaClinicaId);
  }

  // Métodos auxiliares para construir responses
  private EvolucionMedicaResponse construirResponseCompleto(EvolucionMedica evolucionMedica) {
    return EvolucionMedicaResponse.builder()
        .id(evolucionMedica.getId())
        .historiaClinicaId(evolucionMedica.getHistoriaClinica().getId())
        .numeroHistoriaClinica(evolucionMedica.getHistoriaClinica().getNumeroHistoriaClinica())
        .empleadoId(evolucionMedica.getEmpleado().getId())
        .empleadoNombreCompleto(evolucionMedica.getEmpleado().getNombre() + " " + evolucionMedica.getEmpleado().getApellido())
        .empleadoEspecialidad(evolucionMedica.getEmpleado().getEspecialidad())
        .fechaConsulta(evolucionMedica.getFechaConsulta())
        .tipoConsulta(evolucionMedica.getTipoConsulta())
        .estado(evolucionMedica.getEstado())
        .observacionesGenerales(evolucionMedica.getObservacionesGenerales())
        .fechaCreacion(evolucionMedica.getFechaCreacion())
        .fechaActualizacion(evolucionMedica.getFechaActualizacion())
        .motivoAtencion(construirMotivoAtencionResponse(evolucionMedica.getMotivoAtencion()))
        .antecedentesIncidente(construirAntecedentesIncidenteResponse(evolucionMedica.getAntecedentesIncidente()))
        .signosVitales(construirSignosVitalesResponse(evolucionMedica.getSignosVitales()))
        .valoracionClinica(construirValoracionClinicaResponse(evolucionMedica.getValoracionClinica()))
        .localizacionLesiones(construirLocalizacionLesionesResponse(evolucionMedica.getLocalizacionLesiones()))
        .examenesSolicitados(construirExamenesSolicitadosResponse(evolucionMedica.getExamenesSolicitados()))
        .diagnosticos(construirDiagnosticosResponse(evolucionMedica.getDiagnosticos()))
        .planesTratamiento(construirPlanesTratamientoResponse(evolucionMedica.getPlanesTratamiento()))
        .emergenciaObstetrica(construirEmergenciaObstetricaResponse(evolucionMedica.getEmergenciaObstetrica()))
        .altaMedica(construirAltaMedicaResponse(evolucionMedica.getAltaMedica()))
        .build();
  }

  private EvolucionMedicaResumenResponse construirResumenResponse(EvolucionMedica evolucionMedica) {
    boolean tieneSignosVitales = signosVitalesRepository.existsByEvolucionMedicaId(evolucionMedica.getId());
    boolean tieneDiagnosticos = diagnosticosRepository.findByEvolucionMedicaId(evolucionMedica.getId()).size() > 0;
    boolean tieneTratamientos = planesTratamientoRepository.findByEvolucionMedicaId(evolucionMedica.getId()).size() > 0;

    return EvolucionMedicaResumenResponse.builder()
        .id(evolucionMedica.getId())
        .historiaClinicaId(evolucionMedica.getHistoriaClinica().getId())
        .numeroHistoriaClinica(evolucionMedica.getHistoriaClinica().getNumeroHistoriaClinica())
        .empleadoId(evolucionMedica.getEmpleado().getId())
        .empleadoNombreCompleto(evolucionMedica.getEmpleado().getNombre() + " " + evolucionMedica.getEmpleado().getApellido())
        .fechaConsulta(evolucionMedica.getFechaConsulta())
        .tipoConsulta(evolucionMedica.getTipoConsulta())
        .estado(evolucionMedica.getEstado())
        .fechaCreacion(evolucionMedica.getFechaCreacion())
        .tieneSignosVitales(tieneSignosVitales)
        .tieneDiagnosticos(tieneDiagnosticos)
        .tieneTratamientos(tieneTratamientos)
        .build();
  }

  // Métodos auxiliares para construir responses de cada sección
  private EvolucionMotivoAtencionResponse construirMotivoAtencionResponse(EvolucionMotivoAtencion motivoAtencion) {
    if (motivoAtencion == null) return null;

    return EvolucionMotivoAtencionResponse.builder()
        .id(motivoAtencion.getId())
        .evolucionMedicaId(motivoAtencion.getEvolucionMedica().getId())
        .motivoConsulta(motivoAtencion.getMotivoConsulta())
        .enfermedadActual(motivoAtencion.getEnfermedadActual())
        .fechaCreacion(motivoAtencion.getFechaCreacion())
        .build();
  }

  private EvolucionAntecedentesIncidenteResponse construirAntecedentesIncidenteResponse(EvolucionAntecedentesIncidente antecedentes) {
    if (antecedentes == null) return null;

    return EvolucionAntecedentesIncidenteResponse.builder()
        .id(antecedentes.getId())
        .evolucionMedicaId(antecedentes.getEvolucionMedica().getId())
        .antecedentesPersonales(antecedentes.getAntecedentesPersonales())
        .antecedentesFamiliares(antecedentes.getAntecedentesFamiliares())
        .habitosToxicos(antecedentes.getHabitosToxicos())
        .alergias(antecedentes.getAlergias())
        .medicamentosActuales(antecedentes.getMedicamentosActuales())
        .fechaCreacion(antecedentes.getFechaCreacion())
        .build();
  }

  private EvolucionSignosVitalesResponse construirSignosVitalesResponse(EvolucionSignosVitales signosVitales) {
    if (signosVitales == null) return null;

    return EvolucionSignosVitalesResponse.builder()
        .id(signosVitales.getId())
        .evolucionMedicaId(signosVitales.getEvolucionMedica().getId())
        .presionArterialSistolica(signosVitales.getPresionArterialSistolica())
        .presionArterialDiastolica(signosVitales.getPresionArterialDiastolica())
        .frecuenciaCardiaca(signosVitales.getFrecuenciaCardiaca())
        .frecuenciaRespiratoria(signosVitales.getFrecuenciaRespiratoria())
        .temperatura(signosVitales.getTemperatura())
        .saturacionOxigeno(signosVitales.getSaturacionOxigeno())
        .peso(signosVitales.getPeso())
        .talla(signosVitales.getTalla())
        .imc(signosVitales.getImc())
        .glucosa(signosVitales.getGlucosa())
        .fechaCreacion(signosVitales.getFechaCreacion())
        .build();
  }

  // Implementar métodos similares para las demás secciones...
  private EvolucionValoracionClinicaResponse construirValoracionClinicaResponse(EvolucionValoracionClinica valoracionClinica) {
    if (valoracionClinica == null) return null;

    return EvolucionValoracionClinicaResponse.builder()
        .id(valoracionClinica.getId())
        .evolucionMedicaId(valoracionClinica.getEvolucionMedica().getId())
        .inspeccionGeneral(valoracionClinica.getInspeccionGeneral())
        .cabezaCuello(valoracionClinica.getCabezaCuello())
        .torax(valoracionClinica.getTorax())
        .abdomen(valoracionClinica.getAbdomen())
        .extremidades(valoracionClinica.getExtremidades())
        .neurologico(valoracionClinica.getNeurologico())
        .pielTegumentos(valoracionClinica.getPielTegumentos())
        .otrosHallazgos(valoracionClinica.getOtrosHallazgos())
        .fechaCreacion(valoracionClinica.getFechaCreacion())
        .build();
  }

  private List<EvolucionLocalizacionLesionesResponse> construirLocalizacionLesionesResponse(List<EvolucionLocalizacionLesiones> lesiones) {
    if (lesiones == null || lesiones.isEmpty()) return new ArrayList<>();

    return lesiones.stream()
        .map(lesion -> EvolucionLocalizacionLesionesResponse.builder()
            .id(lesion.getId())
            .evolucionMedicaId(lesion.getEvolucionMedica().getId())
            .localizacion(lesion.getLocalizacion())
            .tipoLesion(lesion.getTipoLesion())
            .descripcion(lesion.getDescripcion())
            .gravedad(lesion.getGravedad())
            .fechaCreacion(lesion.getFechaCreacion())
            .build())
        .collect(Collectors.toList());
  }

  private List<EvolucionExamenesSolicitadosResponse> construirExamenesSolicitadosResponse(List<EvolucionExamenesSolicitados> examenes) {
    if (examenes == null || examenes.isEmpty()) return new ArrayList<>();

    return examenes.stream()
        .map(examen -> EvolucionExamenesSolicitadosResponse.builder()
            .id(examen.getId())
            .evolucionMedicaId(examen.getEvolucionMedica().getId())
            .tipoExamen(examen.getTipoExamen())
            .nombreExamen(examen.getNombreExamen())
            .indicaciones(examen.getIndicaciones())
            .urgencia(examen.getUrgencia())
            .estado(examen.getEstado())
            .fechaCreacion(examen.getFechaCreacion())
            .build())
        .collect(Collectors.toList());
  }

  private List<EvolucionDiagnosticosResponse> construirDiagnosticosResponse(List<EvolucionDiagnosticos> diagnosticos) {
    if (diagnosticos == null || diagnosticos.isEmpty()) return new ArrayList<>();

    return diagnosticos.stream()
        .map(diagnostico -> EvolucionDiagnosticosResponse.builder()
            .id(diagnostico.getId())
            .evolucionMedicaId(diagnostico.getEvolucionMedica().getId())
            .codigoCie(diagnostico.getCodigoCie())
            .diagnostico(diagnostico.getDiagnostico())
            .tipo(diagnostico.getTipo())
            .observaciones(diagnostico.getObservaciones())
            .fechaCreacion(diagnostico.getFechaCreacion())
            .build())
        .collect(Collectors.toList());
  }

  private List<EvolucionPlanesTratamientoResponse> construirPlanesTratamientoResponse(List<EvolucionPlanesTratamiento> planes) {
    if (planes == null || planes.isEmpty()) return new ArrayList<>();

    return planes.stream()
        .map(plan -> {
          List<EvolucionIndicacionesMedicasResponse> indicaciones = plan.getIndicacionesMedicas().stream()
              .map(indicacion -> EvolucionIndicacionesMedicasResponse.builder()
                  .id(indicacion.getId())
                  .planTratamientoId(indicacion.getPlanTratamiento().getId())
                  .medicamento(indicacion.getMedicamento())
                  .dosis(indicacion.getDosis())
                  .frecuencia(indicacion.getFrecuencia())
                  .viaAdministracion(indicacion.getViaAdministracion())
                  .duracion(indicacion.getDuracion())
                  .indicacionesEspeciales(indicacion.getIndicacionesEspeciales())
                  .fechaCreacion(indicacion.getFechaCreacion())
                  .build())
              .collect(Collectors.toList());

          return EvolucionPlanesTratamientoResponse.builder()
              .id(plan.getId())
              .evolucionMedicaId(plan.getEvolucionMedica().getId())
              .nombreTratamiento(plan.getNombreTratamiento())
              .descripcion(plan.getDescripcion())
              .tipoTratamiento(plan.getTipoTratamiento())
              .duracion(plan.getDuracion())
              .fechaCreacion(plan.getFechaCreacion())
              .indicacionesMedicas(indicaciones)
              .build();
        })
        .collect(Collectors.toList());
  }

  private EvolucionEmergenciaObstetricaResponse construirEmergenciaObstetricaResponse(EvolucionEmergenciaObstetrica emergencia) {
    if (emergencia == null) return null;

    return EvolucionEmergenciaObstetricaResponse.builder()
        .id(emergencia.getId())
        .evolucionMedicaId(emergencia.getEvolucionMedica().getId())
        .gestasPrevias(emergencia.getGestasPrevias())
        .partosPrevios(emergencia.getPartosPrevios())
        .abortosPrevios(emergencia.getAbortosPrevios())
        .fum(emergencia.getFum())
        .fpp(emergencia.getFpp())
        .semanasGestacion(emergencia.getSemanasGestacion())
        .presentacion(emergencia.getPresentacion())
        .dinamicaUterina(emergencia.getDinamicaUterina())
        .latidosFetales(emergencia.getLatidosFetales())
        .observaciones(emergencia.getObservaciones())
        .fechaCreacion(emergencia.getFechaCreacion())
        .build();
  }

  private EvolucionAltaMedicaResponse construirAltaMedicaResponse(EvolucionAltaMedica altaMedica) {
    if (altaMedica == null) return null;

    return EvolucionAltaMedicaResponse.builder()
        .id(altaMedica.getId())
        .evolucionMedicaId(altaMedica.getEvolucionMedica().getId())
        .fechaAlta(altaMedica.getFechaAlta())
        .tipoAlta(altaMedica.getTipoAlta())
        .condicionAlta(altaMedica.getCondicionAlta())
        .recomendaciones(altaMedica.getRecomendaciones())
        .controlProgramado(altaMedica.getControlProgramado())
        .especialidadControl(altaMedica.getEspecialidadControl())
        .fechaCreacion(altaMedica.getFechaCreacion())
        .build();
  }
}