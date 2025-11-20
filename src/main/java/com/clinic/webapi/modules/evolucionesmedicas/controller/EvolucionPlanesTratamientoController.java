package com.clinic.webapi.modules.evolucionesmedicas.controller;

import com.clinic.webapi.shared.dto.ApiResponse;
import com.clinic.webapi.modules.evolucionesmedicas.dto.EvolucionPlanesTratamientoRequest;
import com.clinic.webapi.modules.evolucionesmedicas.dto.EvolucionPlanesTratamientoResponse;
import com.clinic.webapi.modules.evolucionesmedicas.service.EvolucionPlanesTratamientoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/evoluciones-medicas/{evolucionId}/planes-tratamiento")
@RequiredArgsConstructor
public class EvolucionPlanesTratamientoController {

  private final EvolucionPlanesTratamientoService planesTratamientoService;

  @PostMapping
  @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MEDICO')")
  public ResponseEntity<ApiResponse<EvolucionPlanesTratamientoResponse>> crearPlanTratamiento(
      @PathVariable UUID evolucionId,
      @Valid @RequestBody EvolucionPlanesTratamientoRequest request) {
    try {
      EvolucionPlanesTratamientoResponse response = planesTratamientoService.crearPlanTratamiento(evolucionId, request);
      return ResponseEntity.status(HttpStatus.CREATED)
          .body(ApiResponse.success("Plan de tratamiento creado exitosamente", response));
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest()
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  @GetMapping
  @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MEDICO', 'ENFERMERO')")
  public ResponseEntity<ApiResponse<List<EvolucionPlanesTratamientoResponse>>> obtenerPlanesTratamiento(
      @PathVariable UUID evolucionId) {
    try {
      List<EvolucionPlanesTratamientoResponse> planes = planesTratamientoService.obtenerPlanesTratamiento(evolucionId);
      return ResponseEntity.ok(ApiResponse.success("Planes de tratamiento obtenidos exitosamente", planes));
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  @GetMapping("/{planId}")
  @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MEDICO', 'ENFERMERO')")
  public ResponseEntity<ApiResponse<EvolucionPlanesTratamientoResponse>> obtenerPlanTratamientoPorId(
      @PathVariable UUID evolucionId,
      @PathVariable UUID planId) {
    try {
      EvolucionPlanesTratamientoResponse response = planesTratamientoService.obtenerPlanTratamientoPorId(planId);
      return ResponseEntity.ok(ApiResponse.success("Plan de tratamiento obtenido exitosamente", response));
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  @PutMapping("/{planId}")
  @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MEDICO')")
  public ResponseEntity<ApiResponse<EvolucionPlanesTratamientoResponse>> actualizarPlanTratamiento(
      @PathVariable UUID evolucionId,
      @PathVariable UUID planId,
      @Valid @RequestBody EvolucionPlanesTratamientoRequest request) {
    try {
      EvolucionPlanesTratamientoResponse response = planesTratamientoService.actualizarPlanTratamiento(planId, request);
      return ResponseEntity.ok(ApiResponse.success("Plan de tratamiento actualizado exitosamente", response));
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest()
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  @DeleteMapping("/{planId}")
  @PreAuthorize("hasRole('ADMINISTRADOR')")
  public ResponseEntity<ApiResponse<Void>> eliminarPlanTratamiento(
      @PathVariable UUID evolucionId,
      @PathVariable UUID planId) {
    try {
      planesTratamientoService.eliminarPlanTratamiento(planId);
      return ResponseEntity.ok(ApiResponse.success("Plan de tratamiento eliminado exitosamente"));
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(ApiResponse.error(e.getMessage()));
    }
  }
}