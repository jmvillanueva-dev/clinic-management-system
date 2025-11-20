package com.clinic.webapi.modules.evolucionesmedicas.controller;

import com.clinic.webapi.shared.dto.ApiResponse;
import com.clinic.webapi.modules.evolucionesmedicas.dto.EvolucionSignosVitalesRequest;
import com.clinic.webapi.modules.evolucionesmedicas.dto.EvolucionSignosVitalesResponse;
import com.clinic.webapi.modules.evolucionesmedicas.service.EvolucionSignosVitalesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/evoluciones-medicas/{evolucionId}/signos-vitales")
@RequiredArgsConstructor
public class EvolucionSignosVitalesController {

  private final EvolucionSignosVitalesService signosVitalesService;

  @PostMapping
  @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MEDICO')")
  public ResponseEntity<ApiResponse<EvolucionSignosVitalesResponse>> crearSignosVitales(
      @PathVariable UUID evolucionId,
      @Valid @RequestBody EvolucionSignosVitalesRequest request) {
    try {
      EvolucionSignosVitalesResponse response = signosVitalesService.crearSignosVitales(evolucionId, request);
      return ResponseEntity.status(HttpStatus.CREATED)
          .body(ApiResponse.success("Signos vitales registrados exitosamente", response));
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest()
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  @GetMapping
  @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MEDICO', 'ENFERMERO')")
  public ResponseEntity<ApiResponse<EvolucionSignosVitalesResponse>> obtenerSignosVitales(
      @PathVariable UUID evolucionId) {
    try {
      EvolucionSignosVitalesResponse response = signosVitalesService.obtenerSignosVitales(evolucionId);
      return ResponseEntity.ok(ApiResponse.success("Signos vitales obtenidos exitosamente", response));
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  @PutMapping
  @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MEDICO')")
  public ResponseEntity<ApiResponse<EvolucionSignosVitalesResponse>> actualizarSignosVitales(
      @PathVariable UUID evolucionId,
      @Valid @RequestBody EvolucionSignosVitalesRequest request) {
    try {
      EvolucionSignosVitalesResponse response = signosVitalesService.actualizarSignosVitales(evolucionId, request);
      return ResponseEntity.ok(ApiResponse.success("Signos vitales actualizados exitosamente", response));
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest()
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  @DeleteMapping
  @PreAuthorize("hasRole('ADMINISTRADOR')")
  public ResponseEntity<ApiResponse<Void>> eliminarSignosVitales(@PathVariable UUID evolucionId) {
    try {
      signosVitalesService.eliminarSignosVitales(evolucionId);
      return ResponseEntity.ok(ApiResponse.success("Signos vitales eliminados exitosamente"));
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(ApiResponse.error(e.getMessage()));
    }
  }
}