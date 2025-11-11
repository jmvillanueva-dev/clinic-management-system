package com.clinic.webapi.modules.historiasclinicas.service;

import com.clinic.webapi.modules.historiasclinicas.dto.HistoriaClinicaRequest;
import com.clinic.webapi.modules.historiasclinicas.dto.HistoriaClinicaResponse;
import com.clinic.webapi.modules.historiasclinicas.entity.HistoriaClinica;
import com.clinic.webapi.modules.historiasclinicas.model.mapper.HistoriaClinicaMapper;
import com.clinic.webapi.modules.historiasclinicas.repository.HistoriaClinicaRepository;
import com.clinic.webapi.modules.pacientes.model.entity.Paciente;
import com.clinic.webapi.modules.pacientes.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HistoriaClinicaService {

  private final HistoriaClinicaRepository historiaClinicaRepository;
  private final PacienteRepository pacienteRepository;
  private final HistoriaClinicaMapper historiaClinicaMapper;

  @Transactional
  public HistoriaClinica crearHistoriaClinicaAutomatica(Paciente paciente) {
    // Verificar si ya existe historia clínica
    if (historiaClinicaRepository.existsByPaciente(paciente)) {
      throw new RuntimeException("El paciente ya tiene una historia clínica");
    }

    // Verificar si ya existe el número de historia clínica
    if (historiaClinicaRepository.existsByNumeroHistoriaClinica(paciente.getCedula())) {
      throw new RuntimeException("Ya existe una historia clínica con el número: " + paciente.getCedula());
    }

    HistoriaClinica historiaClinica = HistoriaClinica.builder()
        .paciente(paciente)
        .numeroHistoriaClinica(paciente.getCedula())
        .build();

    return historiaClinicaRepository.save(historiaClinica);
  }

  @Transactional
  public HistoriaClinicaResponse crearHistoriaClinica(HistoriaClinicaRequest request) {
    Paciente paciente = pacienteRepository.findById(request.getPacienteId())
        .orElseThrow(() -> new RuntimeException("Paciente no encontrado con ID: " + request.getPacienteId()));

    // Verificar si ya existe historia clínica
    if (historiaClinicaRepository.existsByPaciente(paciente)) {
      throw new RuntimeException("El paciente ya tiene una historia clínica");
    }

    // Verificar si ya existe el número de historia clínica
    String numeroHistoriaClinica = paciente.getCedula();
    if (historiaClinicaRepository.existsByNumeroHistoriaClinica(numeroHistoriaClinica)) {
      throw new RuntimeException("Ya existe una historia clínica con el número: " + numeroHistoriaClinica);
    }

    HistoriaClinica historiaClinica = historiaClinicaMapper.toEntity(request);
    historiaClinica.setPaciente(paciente);
    historiaClinica.setNumeroHistoriaClinica(numeroHistoriaClinica);

    HistoriaClinica historiaGuardada = historiaClinicaRepository.save(historiaClinica);
    return enrichResponse(historiaClinicaMapper.toResponse(historiaGuardada));
  }

  @Transactional(readOnly = true)
  public HistoriaClinicaResponse obtenerHistoriaClinicaPorId(UUID id) {
    HistoriaClinica historiaClinica = historiaClinicaRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Historia clínica no encontrada con ID: " + id));
    return enrichResponse(historiaClinicaMapper.toResponse(historiaClinica));
  }

  @Transactional(readOnly = true)
  public HistoriaClinicaResponse obtenerHistoriaClinicaPorPacienteId(UUID pacienteId) {
    HistoriaClinica historiaClinica = historiaClinicaRepository.findByPacienteId(pacienteId)
        .orElseThrow(() -> new RuntimeException("Historia clínica no encontrada para el paciente con ID: " + pacienteId));
    return enrichResponse(historiaClinicaMapper.toResponse(historiaClinica));
  }

  @Transactional(readOnly = true)
  public HistoriaClinicaResponse obtenerHistoriaClinicaPorNumero(String numeroHistoriaClinica) {
    HistoriaClinica historiaClinica = historiaClinicaRepository.findByNumeroHistoriaClinica(numeroHistoriaClinica)
        .orElseThrow(() -> new RuntimeException("Historia clínica no encontrada con número: " + numeroHistoriaClinica));
    return enrichResponse(historiaClinicaMapper.toResponse(historiaClinica));
  }

  @Transactional(readOnly = true)
  public List<HistoriaClinicaResponse> obtenerTodasLasHistoriasClinicas() {
    return historiaClinicaRepository.findAll()
        .stream()
        .map(historiaClinicaMapper::toResponse)
        .map(this::enrichResponse)
        .collect(Collectors.toList());
  }

  @Transactional
  public HistoriaClinicaResponse actualizarHistoriaClinica(UUID id, HistoriaClinicaRequest request) {
    HistoriaClinica historiaClinicaExistente = historiaClinicaRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Historia clínica no encontrada con ID: " + id));

    HistoriaClinica historiaActualizada = historiaClinicaMapper.updateEntityFromRequest(historiaClinicaExistente, request);
    HistoriaClinica historiaGuardada = historiaClinicaRepository.save(historiaActualizada);

    return enrichResponse(historiaClinicaMapper.toResponse(historiaGuardada));
  }

  @Transactional
  public void eliminarHistoriaClinica(UUID id) {
    HistoriaClinica historiaClinica = historiaClinicaRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Historia clínica no encontrada con ID: " + id));
    historiaClinicaRepository.delete(historiaClinica);
  }

  // Método auxiliar para enriquecer la respuesta con datos adicionales
  private HistoriaClinicaResponse enrichResponse(HistoriaClinicaResponse response) {
    Long totalEvoluciones = historiaClinicaRepository.countEvolucionesByHistoriaClinicaId(response.getId());
    response.setTotalEvoluciones(totalEvoluciones != null ? totalEvoluciones.intValue() : 0);
    return response;
  }

  // Método para obtener la entidad por paciente (uso interno)
  public HistoriaClinica obtenerEntidadPorPacienteId(UUID pacienteId) {
    return historiaClinicaRepository.findByPacienteId(pacienteId)
        .orElseThrow(() -> new RuntimeException("Historia clínica no encontrada para el paciente con ID: " + pacienteId));
  }
}