package com.clinic.webapi.modules.pacientes.service;

import com.clinic.webapi.modules.historiasclinicas.service.HistoriaClinicaService;
import com.clinic.webapi.modules.pacientes.dto.PacienteRequest;
import com.clinic.webapi.modules.pacientes.dto.PacienteResponse;
import com.clinic.webapi.modules.pacientes.model.entity.Paciente;
import com.clinic.webapi.modules.pacientes.model.mapper.PacienteMapper;
import com.clinic.webapi.modules.pacientes.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PacienteService {

  private final PacienteRepository pacienteRepository;
  private final HistoriaClinicaService historiaClinicaService;
  private final PacienteMapper pacienteMapper;

  @Transactional
  public PacienteResponse crearPaciente(PacienteRequest request) {
    // Validaciones
    if (pacienteRepository.existsByCedula(request.getCedula())) {
      throw new RuntimeException("Ya existe un paciente con la cédula: " + request.getCedula());
    }

    // Mapear y guardar
    Paciente paciente = pacienteMapper.toEntity(request);
    Paciente pacienteGuardado = pacienteRepository.save(paciente);

    // Crear historia clínica automáticamente
    try {
      historiaClinicaService.crearHistoriaClinicaAutomatica(pacienteGuardado);
      System.out.println("Historia clínica creada automáticamente para el paciente: " + pacienteGuardado.getCedula());
    } catch (Exception e) {
      // Log del error pero no revertir la creación del paciente
      System.err.println("Error creando historia clínica automática: " + e.getMessage());
      // Podrías enviar una notificación o registrar en logs de aplicación
    }

    return pacienteMapper.toResponse(pacienteGuardado);
  }

  @Transactional(readOnly = true)
  public List<PacienteResponse> obtenerTodosLosPacientes() {
    return pacienteRepository.findAllByEstaActivo(true)
        .stream()
        .map(pacienteMapper::toResponse)
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public PacienteResponse obtenerPacientePorId(UUID id) {
    Paciente paciente = pacienteRepository.findByIdWithDetails(id)
        .orElseThrow(() -> new RuntimeException("Paciente no encontrado con ID: " + id));
    return pacienteMapper.toResponse(paciente);
  }

  @Transactional(readOnly = true)
  public PacienteResponse obtenerPacientePorCedula(String cedula) {
    Paciente paciente = pacienteRepository.findByCedula(cedula)
        .orElseThrow(() -> new RuntimeException("Paciente no encontrado con cédula: " + cedula));
    return pacienteMapper.toResponse(paciente);
  }

  @Transactional
  public PacienteResponse actualizarPaciente(UUID id, PacienteRequest request) {
    Paciente pacienteExistente = pacienteRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Paciente no encontrado con ID: " + id));

    // Validar cédula si cambió
    if (!request.getCedula().equals(pacienteExistente.getCedula()) &&
        pacienteRepository.existsByCedula(request.getCedula())) {
      throw new RuntimeException("Ya existe un paciente con la cédula: " + request.getCedula());
    }

    Paciente pacienteActualizado = pacienteMapper.updateEntityFromRequest(pacienteExistente, request);
    Paciente pacienteGuardado = pacienteRepository.save(pacienteActualizado);

    return pacienteMapper.toResponse(pacienteGuardado);
  }

  @Transactional
  public void eliminarPaciente(UUID id) {
    Paciente paciente = pacienteRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Paciente no encontrado con ID: " + id));

    paciente.setEstaActivo(false);
    pacienteRepository.save(paciente);
  }

  @Transactional(readOnly = true)
  public List<PacienteResponse> buscarPacientes(String searchTerm) {
    return pacienteRepository.searchActivePacientes(searchTerm)
        .stream()
        .map(pacienteMapper::toResponse)
        .collect(Collectors.toList());
  }
}