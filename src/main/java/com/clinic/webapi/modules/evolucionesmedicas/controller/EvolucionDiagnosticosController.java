package com.clinic.webapi.modules.evolucionesmedicas.controller;

import com.clinic.webapi.shared.dto.ApiResponse;
import com.clinic.webapi.modules.evolucionesmedicas.dto.EvolucionDiagnosticosRequest;
import com.clinic.webapi.modules.evolucionesmedicas.dto.EvolucionDiagnosticosResponse;
import com.clinic.webapi.modules.evolucionesmedicas.service.EvolucionDiagnosticosService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/evoluciones-medicas/{evolucionId}/diagnosticos")
@RequiredArgsConstructor
public class EvolucionDiagnosticosController {

  private final EvolucionDiagnosticosService diagnosticosService;

  @PostMapping
  @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MEDICO')")
  public ResponseEntity<ApiResponse<EvolucionDiagnosticosResponse>> crearDiagnostico(
      @PathVariable UUID evolucionId,
      @Valid @RequestBody EvolucionDiagnosticosRequest request) {
    try {
      EvolucionDiagnosticosResponse response = diagnosticosService.crearDiagnostico(evolucionId, request);
      return ResponseEntity.status(HttpStatus.CREATED)
          .body(ApiResponse.success("Diagnóstico creado exitosamente", response));
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest()
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  @GetMapping
  @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MEDICO', 'ENFERMERO')")
  public ResponseEntity<ApiResponse<List<EvolucionDiagnosticosResponse>>> obtenerDiagnosticos(
      @PathVariable UUID evolucionId) {
    try {
      List<EvolucionDiagnosticosResponse> diagnosticos = diagnosticosService.obtenerDiagnosticos(evolucionId);
      return ResponseEntity.ok(ApiResponse.success("Diagnósticos obtenidos exitosamente", diagnosticos));
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  @GetMapping("/{diagnosticoId}")
  @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MEDICO', 'ENFERMERO')")
  public ResponseEntity<ApiResponse<EvolucionDiagnosticosResponse>> obtenerDiagnosticoPorId(
      @PathVariable UUID evolucionId,
      @PathVariable UUID diagnosticoId) {
    try {
      EvolucionDiagnosticosResponse response = diagnosticosService.obtenerDiagnosticoPorId(diagnosticoId);
      return ResponseEntity.ok(ApiResponse.success("Diagnóstico obtenido exitosamente", response));
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  @PutMapping("/{diagnosticoId}")
  @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MEDICO')")
  public ResponseEntity<ApiResponse<EvolucionDiagnosticosResponse>> actualizarDiagnostico(
      @PathVariable UUID evolucionId,
      @PathVariable UUID diagnosticoId,
      @Valid @RequestBody EvolucionDiagnosticosRequest request) {
    try {
      EvolucionDiagnosticosResponse response = diagnosticosService.actualizarDiagnostico(diagnosticoId, request);
      return ResponseEntity.ok(ApiResponse.success("Diagnóstico actualizado exitosamente", response));
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest()
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  @DeleteMapping("/{diagnosticoId}")
  @PreAuthorize("hasRole('ADMINISTRADOR')")
  public ResponseEntity<ApiResponse<Void>> eliminarDiagnostico(
      @PathVariable UUID evolucionId,
      @PathVariable UUID diagnosticoId) {
    try {
      diagnosticosService.eliminarDiagnostico(diagnosticoId);
      return ResponseEntity.ok(ApiResponse.success("Diagnóstico eliminado exitosamente"));
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(ApiResponse.error(e.getMessage()));
    }
  }
}