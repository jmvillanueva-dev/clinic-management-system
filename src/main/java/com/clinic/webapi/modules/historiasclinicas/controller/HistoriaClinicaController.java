package com.clinic.webapi.modules.historiasclinicas.controller;

import com.clinic.webapi.shared.dto.ApiResponse;
import com.clinic.webapi.modules.historiasclinicas.dto.HistoriaClinicaRequest;
import com.clinic.webapi.modules.historiasclinicas.dto.HistoriaClinicaResponse;
import com.clinic.webapi.modules.historiasclinicas.service.HistoriaClinicaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/historias-clinicas")
@RequiredArgsConstructor
public class HistoriaClinicaController {

  private final HistoriaClinicaService historiaClinicaService;

  @PostMapping
  @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MEDICO')")
  public ResponseEntity<ApiResponse<HistoriaClinicaResponse>> crearHistoriaClinica(
      @Valid @RequestBody HistoriaClinicaRequest request) {
    try {
      HistoriaClinicaResponse response = historiaClinicaService.crearHistoriaClinica(request);
      return ResponseEntity.status(HttpStatus.CREATED)
          .body(ApiResponse.success("Historia clínica creada exitosamente", response));
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest()
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  @GetMapping
  @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MEDICO', 'ENFERMERO')")
  public ResponseEntity<ApiResponse<List<HistoriaClinicaResponse>>> obtenerTodasLasHistoriasClinicas() {
    List<HistoriaClinicaResponse> historias = historiaClinicaService.obtenerTodasLasHistoriasClinicas();
    return ResponseEntity.ok(ApiResponse.success("Historias clínicas obtenidas exitosamente", historias));
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MEDICO', 'ENFERMERO')")
  public ResponseEntity<ApiResponse<HistoriaClinicaResponse>> obtenerHistoriaClinicaPorId(@PathVariable UUID id) {
    try {
      HistoriaClinicaResponse response = historiaClinicaService.obtenerHistoriaClinicaPorId(id);
      return ResponseEntity.ok(ApiResponse.success("Historia clínica obtenida exitosamente", response));
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  @GetMapping("/paciente/{pacienteId}")
  @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MEDICO', 'ENFERMERO')")
  public ResponseEntity<ApiResponse<HistoriaClinicaResponse>> obtenerHistoriaClinicaPorPaciente(@PathVariable UUID pacienteId) {
    try {
      HistoriaClinicaResponse response = historiaClinicaService.obtenerHistoriaClinicaPorPacienteId(pacienteId);
      return ResponseEntity.ok(ApiResponse.success("Historia clínica obtenida exitosamente", response));
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  @GetMapping("/numero/{numeroHistoriaClinica}")
  @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MEDICO', 'ENFERMERO')")
  public ResponseEntity<ApiResponse<HistoriaClinicaResponse>> obtenerHistoriaClinicaPorNumero(@PathVariable String numeroHistoriaClinica) {
    try {
      HistoriaClinicaResponse response = historiaClinicaService.obtenerHistoriaClinicaPorNumero(numeroHistoriaClinica);
      return ResponseEntity.ok(ApiResponse.success("Historia clínica obtenida exitosamente", response));
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  @GetMapping("/buscar-por-fecha")
  @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MEDICO', 'ENFERMERO')")
  public ResponseEntity<ApiResponse<List<HistoriaClinicaResponse>>> buscarHistoriasPorFecha(
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
    try {
      Instant inicio = fechaInicio.atStartOfDay(ZoneId.systemDefault()).toInstant();
      Instant fin = fechaFin.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant();
      List<HistoriaClinicaResponse> historias = historiaClinicaService.buscarHistoriasPorFecha(inicio, fin);
      return ResponseEntity.ok(ApiResponse.success("Historias clínicas encontradas exitosamente", historias));
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
    }
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MEDICO')")
  public ResponseEntity<ApiResponse<HistoriaClinicaResponse>> actualizarHistoriaClinica(
      @PathVariable UUID id,
      @Valid @RequestBody HistoriaClinicaRequest request) {
    try {
      HistoriaClinicaResponse response = historiaClinicaService.actualizarHistoriaClinica(id, request);
      return ResponseEntity.ok(ApiResponse.success("Historia clínica actualizada exitosamente", response));
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest()
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMINISTRADOR')")
  public ResponseEntity<ApiResponse<Void>> eliminarHistoriaClinica(@PathVariable UUID id) {
    try {
      historiaClinicaService.eliminarHistoriaClinica(id);
      return ResponseEntity.ok(ApiResponse.success("Historia clínica eliminada exitosamente"));
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(ApiResponse.error(e.getMessage()));
    }
  }
}
