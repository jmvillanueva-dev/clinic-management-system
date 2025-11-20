package com.clinic.webapi.modules.evolucionesmedicas.service;

import com.clinic.webapi.modules.evolucionesmedicas.dto.*;
import com.clinic.webapi.modules.evolucionesmedicas.entity.EvolucionIndicacionesMedicas;
import com.clinic.webapi.modules.evolucionesmedicas.entity.EvolucionMedica;
import com.clinic.webapi.modules.evolucionesmedicas.entity.EvolucionPlanesTratamiento;
import com.clinic.webapi.modules.evolucionesmedicas.repository.EvolucionIndicacionesMedicasRepository;
import com.clinic.webapi.modules.evolucionesmedicas.repository.EvolucionMedicaRepository;
import com.clinic.webapi.modules.evolucionesmedicas.repository.EvolucionPlanesTratamientoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EvolucionPlanesTratamientoService {

  private final EvolucionPlanesTratamientoRepository planesTratamientoRepository;
  private final EvolucionIndicacionesMedicasRepository indicacionesMedicasRepository;
  private final EvolucionMedicaRepository evolucionMedicaRepository;

  @Transactional
  public EvolucionPlanesTratamientoResponse crearPlanTratamiento(UUID evolucionId, EvolucionPlanesTratamientoRequest request) {
    EvolucionMedica evolucionMedica = evolucionMedicaRepository.findById(evolucionId)
        .orElseThrow(() -> new RuntimeException("Evolución médica no encontrada con ID: " + evolucionId));

    EvolucionPlanesTratamiento planTratamiento = EvolucionPlanesTratamiento.builder()
        .evolucionMedica(evolucionMedica)
        .nombreTratamiento(request.getNombreTratamiento())
        .descripcion(request.getDescripcion())
        .tipoTratamiento(request.getTipoTratamiento())
        .duracion(request.getDuracion())
        .build();

    EvolucionPlanesTratamiento planGuardado = planesTratamientoRepository.save(planTratamiento);

    // Crear indicaciones médicas si existen
    if (request.getIndicacionesMedicas() != null && !request.getIndicacionesMedicas().isEmpty()) {
      crearIndicacionesMedicas(planGuardado, request.getIndicacionesMedicas());
    }

    return construirResponse(planGuardado);
  }

  @Transactional(readOnly = true)
  public List<EvolucionPlanesTratamientoResponse> obtenerPlanesTratamiento(UUID evolucionId) {
    List<EvolucionPlanesTratamiento> planes = planesTratamientoRepository.findByEvolucionMedicaId(evolucionId);

    return planes.stream()
        .map(this::construirResponse)
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public EvolucionPlanesTratamientoResponse obtenerPlanTratamientoPorId(UUID planId) {
    EvolucionPlanesTratamiento plan = planesTratamientoRepository.findById(planId)
        .orElseThrow(() -> new RuntimeException("Plan de tratamiento no encontrado con ID: " + planId));

    return construirResponse(plan);
  }

  @Transactional
  public EvolucionPlanesTratamientoResponse actualizarPlanTratamiento(UUID planId, EvolucionPlanesTratamientoRequest request) {
    EvolucionPlanesTratamiento planExistente = planesTratamientoRepository.findById(planId)
        .orElseThrow(() -> new RuntimeException("Plan de tratamiento no encontrado con ID: " + planId));

    planExistente.setNombreTratamiento(request.getNombreTratamiento());
    planExistente.setDescripcion(request.getDescripcion());
    planExistente.setTipoTratamiento(request.getTipoTratamiento());
    planExistente.setDuracion(request.getDuracion());

    // Actualizar indicaciones médicas
    if (request.getIndicacionesMedicas() != null) {
      // Eliminar indicaciones existentes y crear nuevas
      indicacionesMedicasRepository.deleteByPlanTratamientoId(planId);
      if (!request.getIndicacionesMedicas().isEmpty()) {
        crearIndicacionesMedicas(planExistente, request.getIndicacionesMedicas());
      }
    }

    EvolucionPlanesTratamiento planActualizado = planesTratamientoRepository.save(planExistente);

    return construirResponse(planActualizado);
  }

  @Transactional
  public void eliminarPlanTratamiento(UUID planId) {
    EvolucionPlanesTratamiento plan = planesTratamientoRepository.findById(planId)
        .orElseThrow(() -> new RuntimeException("Plan de tratamiento no encontrado con ID: " + planId));

    planesTratamientoRepository.delete(plan);
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

  private EvolucionPlanesTratamientoResponse construirResponse(EvolucionPlanesTratamiento plan) {
    List<EvolucionIndicacionesMedicasResponse> indicaciones = indicacionesMedicasRepository.findByPlanTratamientoId(plan.getId())
        .stream()
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
  }
}