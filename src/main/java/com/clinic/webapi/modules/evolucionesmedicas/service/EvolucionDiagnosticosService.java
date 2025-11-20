package com.clinic.webapi.modules.evolucionesmedicas.service;

import com.clinic.webapi.modules.evolucionesmedicas.dto.EvolucionDiagnosticosRequest;
import com.clinic.webapi.modules.evolucionesmedicas.dto.EvolucionDiagnosticosResponse;
import com.clinic.webapi.modules.evolucionesmedicas.entity.EvolucionDiagnosticos;
import com.clinic.webapi.modules.evolucionesmedicas.entity.EvolucionMedica;
import com.clinic.webapi.modules.evolucionesmedicas.repository.EvolucionDiagnosticosRepository;
import com.clinic.webapi.modules.evolucionesmedicas.repository.EvolucionMedicaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EvolucionDiagnosticosService {

  private final EvolucionDiagnosticosRepository diagnosticosRepository;
  private final EvolucionMedicaRepository evolucionMedicaRepository;

  @Transactional
  public EvolucionDiagnosticosResponse crearDiagnostico(UUID evolucionId, EvolucionDiagnosticosRequest request) {
    EvolucionMedica evolucionMedica = evolucionMedicaRepository.findById(evolucionId)
        .orElseThrow(() -> new RuntimeException("Evolución médica no encontrada con ID: " + evolucionId));

    EvolucionDiagnosticos diagnostico = EvolucionDiagnosticos.builder()
        .evolucionMedica(evolucionMedica)
        .codigoCie(request.getCodigoCie())
        .diagnostico(request.getDiagnostico())
        .tipo(request.getTipo())
        .observaciones(request.getObservaciones())
        .build();

    EvolucionDiagnosticos diagnosticoGuardado = diagnosticosRepository.save(diagnostico);

    return construirResponse(diagnosticoGuardado);
  }

  @Transactional(readOnly = true)
  public List<EvolucionDiagnosticosResponse> obtenerDiagnosticos(UUID evolucionId) {
    List<EvolucionDiagnosticos> diagnosticos = diagnosticosRepository.findByEvolucionMedicaId(evolucionId);

    return diagnosticos.stream()
        .map(this::construirResponse)
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public EvolucionDiagnosticosResponse obtenerDiagnosticoPorId(UUID diagnosticoId) {
    EvolucionDiagnosticos diagnostico = diagnosticosRepository.findById(diagnosticoId)
        .orElseThrow(() -> new RuntimeException("Diagnóstico no encontrado con ID: " + diagnosticoId));

    return construirResponse(diagnostico);
  }

  @Transactional
  public EvolucionDiagnosticosResponse actualizarDiagnostico(UUID diagnosticoId, EvolucionDiagnosticosRequest request) {
    EvolucionDiagnosticos diagnosticoExistente = diagnosticosRepository.findById(diagnosticoId)
        .orElseThrow(() -> new RuntimeException("Diagnóstico no encontrado con ID: " + diagnosticoId));

    diagnosticoExistente.setCodigoCie(request.getCodigoCie());
    diagnosticoExistente.setDiagnostico(request.getDiagnostico());
    diagnosticoExistente.setTipo(request.getTipo());
    diagnosticoExistente.setObservaciones(request.getObservaciones());

    EvolucionDiagnosticos diagnosticoActualizado = diagnosticosRepository.save(diagnosticoExistente);

    return construirResponse(diagnosticoActualizado);
  }

  @Transactional
  public void eliminarDiagnostico(UUID diagnosticoId) {
    EvolucionDiagnosticos diagnostico = diagnosticosRepository.findById(diagnosticoId)
        .orElseThrow(() -> new RuntimeException("Diagnóstico no encontrado con ID: " + diagnosticoId));

    diagnosticosRepository.delete(diagnostico);
  }

  private EvolucionDiagnosticosResponse construirResponse(EvolucionDiagnosticos diagnostico) {
    return EvolucionDiagnosticosResponse.builder()
        .id(diagnostico.getId())
        .evolucionMedicaId(diagnostico.getEvolucionMedica().getId())
        .codigoCie(diagnostico.getCodigoCie())
        .diagnostico(diagnostico.getDiagnostico())
        .tipo(diagnostico.getTipo())
        .observaciones(diagnostico.getObservaciones())
        .fechaCreacion(diagnostico.getFechaCreacion())
        .build();
  }
}