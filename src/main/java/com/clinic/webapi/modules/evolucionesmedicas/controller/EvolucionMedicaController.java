package com.clinic.webapi.modules.evolucionesmedicas.controller;

import com.clinic.webapi.shared.dto.ApiResponse;
import com.clinic.webapi.modules.evolucionesmedicas.dto.EvolucionMedicaRequest;
import com.clinic.webapi.modules.evolucionesmedicas.dto.EvolucionMedicaResponse;
import com.clinic.webapi.modules.evolucionesmedicas.dto.EvolucionMedicaResumenResponse;
import com.clinic.webapi.modules.evolucionesmedicas.dto.EvolucionMedicaUpdateRequest;
import com.clinic.webapi.modules.evolucionesmedicas.service.EvolucionMedicaService;
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
@RequestMapping("/api/v1/evoluciones-medicas")
@RequiredArgsConstructor
public class EvolucionMedicaController {

  private final EvolucionMedicaService evolucionMedicaService;

  @PostMapping
  @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MEDICO')")
  public ResponseEntity<ApiResponse<EvolucionMedicaResponse>> crearEvolucionMedica(
      @Valid @RequestBody EvolucionMedicaRequest request) {
    try {
      EvolucionMedicaResponse response = evolucionMedicaService.crearEvolucionMedica(request);
      return ResponseEntity.status(HttpStatus.CREATED)
          .body(ApiResponse.success("Evolución médica creada exitosamente", response));
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest()
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MEDICO', 'ENFERMERO')")
  public ResponseEntity<ApiResponse<EvolucionMedicaResponse>> obtenerEvolucionMedicaPorId(@PathVariable UUID id) {
    try {
      EvolucionMedicaResponse response = evolucionMedicaService.obtenerEvolucionMedicaCompleta(id);
      return ResponseEntity.ok(ApiResponse.success("Evolución médica obtenida exitosamente", response));
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  @GetMapping("/historia-clinica/{historiaClinicaId}")
  @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MEDICO', 'ENFERMERO')")
  public ResponseEntity<ApiResponse<List<EvolucionMedicaResumenResponse>>> obtenerEvolucionesPorHistoriaClinica(
      @PathVariable UUID historiaClinicaId) {
    try {
      List<EvolucionMedicaResumenResponse> evoluciones =
          evolucionMedicaService.obtenerEvolucionesPorHistoriaClinica(historiaClinicaId);
      return ResponseEntity.ok(ApiResponse.success("Evoluciones médicas obtenidas exitosamente", evoluciones));
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  @GetMapping("/empleado/{empleadoId}")
  @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MEDICO')")
  public ResponseEntity<ApiResponse<List<EvolucionMedicaResumenResponse>>> obtenerEvolucionesPorEmpleado(
      @PathVariable UUID empleadoId) {
    try {
      List<EvolucionMedicaResumenResponse> evoluciones =
          evolucionMedicaService.obtenerEvolucionesPorEmpleado(empleadoId);
      return ResponseEntity.ok(ApiResponse.success("Evoluciones médicas obtenidas exitosamente", evoluciones));
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  @GetMapping("/filtros")
  @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MEDICO')")
  public ResponseEntity<ApiResponse<List<EvolucionMedicaResumenResponse>>> obtenerEvolucionesPorFiltros(
      @RequestParam(required = false) UUID historiaClinicaId,
      @RequestParam(required = false) UUID empleadoId,
      @RequestParam(required = false) String estado,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {

    try {
      // Convertir LocalDate a Instant
      Instant fechaInicioInstant = fechaInicio != null ?
          fechaInicio.atStartOfDay(ZoneId.systemDefault()).toInstant() : null;
      Instant fechaFinInstant = fechaFin != null ?
          fechaFin.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant() : null;

      List<EvolucionMedicaResumenResponse> evoluciones =
          evolucionMedicaService.obtenerEvolucionesPorFiltros(
              historiaClinicaId, empleadoId, estado, fechaInicioInstant, fechaFinInstant);

      return ResponseEntity.ok(ApiResponse.success("Evoluciones médicas obtenidas exitosamente", evoluciones));
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest()
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MEDICO')")
  public ResponseEntity<ApiResponse<EvolucionMedicaResponse>> actualizarEvolucionMedica(
      @PathVariable UUID id,
      @Valid @RequestBody EvolucionMedicaUpdateRequest request) {
    try {
      EvolucionMedicaResponse response = evolucionMedicaService.actualizarEvolucionMedica(id, request);
      return ResponseEntity.ok(ApiResponse.success("Evolución médica actualizada exitosamente", response));
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest()
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMINISTRADOR')")
  public ResponseEntity<ApiResponse<Void>> eliminarEvolucionMedica(@PathVariable UUID id) {
    try {
      evolucionMedicaService.eliminarEvolucionMedica(id);
      return ResponseEntity.ok(ApiResponse.success("Evolución médica eliminada exitosamente"));
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  @GetMapping("/historia-clinica/{historiaClinicaId}/count")
  @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MEDICO', 'ENFERMERO')")
  public ResponseEntity<ApiResponse<Long>> contarEvolucionesPorHistoriaClinica(@PathVariable UUID historiaClinicaId) {
    try {
      Long count = evolucionMedicaService.contarEvolucionesPorHistoriaClinica(historiaClinicaId);
      return ResponseEntity.ok(ApiResponse.success("Conteo obtenido exitosamente", count));
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(ApiResponse.error(e.getMessage()));
    }
  }
}