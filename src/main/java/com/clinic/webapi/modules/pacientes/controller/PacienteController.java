package com.clinic.webapi.modules.pacientes.controller;

import com.clinic.webapi.shared.dto.ApiResponse;
import com.clinic.webapi.modules.pacientes.dto.PacienteRequest;
import com.clinic.webapi.modules.pacientes.dto.PacienteResponse;
import com.clinic.webapi.modules.pacientes.service.PacienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/pacientes")
@RequiredArgsConstructor
public class PacienteController {

  private final PacienteService pacienteService;

  @PostMapping
  @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MEDICO', 'ENFERMERO')")
  public ResponseEntity<ApiResponse<PacienteResponse>> crearPaciente(
      @Valid @RequestBody PacienteRequest request) {
    try {
      PacienteResponse response = pacienteService.crearPaciente(request);
      return ResponseEntity.ok(ApiResponse.success("Paciente creado exitosamente", response));
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest()
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  @GetMapping
  @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MEDICO', 'ENFERMERO')")
  public ResponseEntity<ApiResponse<List<PacienteResponse>>> obtenerTodosLosPacientes() {
    List<PacienteResponse> pacientes = pacienteService.obtenerTodosLosPacientes();
    return ResponseEntity.ok(ApiResponse.success("Pacientes obtenidos exitosamente", pacientes));
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MEDICO', 'ENFERMERO')")
  public ResponseEntity<ApiResponse<PacienteResponse>> obtenerPacientePorId(@PathVariable UUID id) {
    try {
      PacienteResponse response = pacienteService.obtenerPacientePorId(id);
      return ResponseEntity.ok(ApiResponse.success("Paciente obtenido exitosamente", response));
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  @GetMapping("/cedula/{cedula}")
  @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MEDICO', 'ENFERMERO')")
  public ResponseEntity<ApiResponse<PacienteResponse>> obtenerPacientePorCedula(@PathVariable String cedula) {
    try {
      PacienteResponse response = pacienteService.obtenerPacientePorCedula(cedula);
      return ResponseEntity.ok(ApiResponse.success("Paciente obtenido exitosamente", response));
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MEDICO', 'ENFERMERO')")
  public ResponseEntity<ApiResponse<PacienteResponse>> actualizarPaciente(
      @PathVariable UUID id,
      @Valid @RequestBody PacienteRequest request) {
    try {
      PacienteResponse response = pacienteService.actualizarPaciente(id, request);
      return ResponseEntity.ok(ApiResponse.success("Paciente actualizado exitosamente", response));
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest()
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMINISTRADOR')")
  public ResponseEntity<ApiResponse<Void>> eliminarPaciente(@PathVariable UUID id) {
    try {
      pacienteService.eliminarPaciente(id);
      return ResponseEntity.ok(ApiResponse.success("Paciente eliminado exitosamente"));
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  @GetMapping("/buscar")
  @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MEDICO', 'ENFERMERO')")
  public ResponseEntity<ApiResponse<List<PacienteResponse>>> buscarPacientes(
      @RequestParam String search) {
    List<PacienteResponse> pacientes = pacienteService.buscarPacientes(search);
    return ResponseEntity.ok(ApiResponse.success("Búsqueda completada", pacientes));
  }
}