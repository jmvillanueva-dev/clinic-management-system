package com.clinic.webapi.modules.catalogos.controller;

import com.clinic.webapi.shared.dto.ApiResponse;
import com.clinic.webapi.modules.catalogos.dto.CatalogoRequest;
import com.clinic.webapi.modules.catalogos.dto.CatalogoResponse;
import com.clinic.webapi.modules.catalogos.service.CatalogoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/catalogos")
@RequiredArgsConstructor
public class CatalogoController {

  private final CatalogoService catalogoService;

  @PostMapping
  @PreAuthorize("hasRole('ADMINISTRADOR')")
  public ResponseEntity<ApiResponse<CatalogoResponse>> crearCatalogo(
      @Valid @RequestBody CatalogoRequest request) {
    try {
      CatalogoResponse response = catalogoService.crearCatalogo(request);
      return ResponseEntity.status(HttpStatus.CREATED)
          .body(ApiResponse.success("Catálogo creado exitosamente", response));
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest()
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  @GetMapping
  @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MEDICO', 'ENFERMERO')")
  public ResponseEntity<ApiResponse<List<CatalogoResponse>>> obtenerTodosLosCatalogos() {
    List<CatalogoResponse> catalogos = catalogoService.obtenerTodosLosCatalogos();
    return ResponseEntity.ok(ApiResponse.success("Catálogos obtenidos exitosamente", catalogos));
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MEDICO', 'ENFERMERO')")
  public ResponseEntity<ApiResponse<CatalogoResponse>> obtenerCatalogoPorId(@PathVariable UUID id) {
    try {
      CatalogoResponse response = catalogoService.obtenerCatalogoPorId(id);
      return ResponseEntity.ok(ApiResponse.success("Catálogo obtenido exitosamente", response));
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  @GetMapping("/tipo/{tipo}")
  @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MEDICO', 'ENFERMERO')")
  public ResponseEntity<ApiResponse<List<CatalogoResponse>>> obtenerCatalogosPorTipo(@PathVariable String tipo) {
    List<CatalogoResponse> catalogos = catalogoService.obtenerCatalogosPorTipo(tipo);
    return ResponseEntity.ok(ApiResponse.success("Catálogos obtenidos exitosamente", catalogos));
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMINISTRADOR')")
  public ResponseEntity<ApiResponse<CatalogoResponse>> actualizarCatalogo(
      @PathVariable UUID id,
      @Valid @RequestBody CatalogoRequest request) {
    try {
      CatalogoResponse response = catalogoService.actualizarCatalogo(id, request);
      return ResponseEntity.ok(ApiResponse.success("Catálogo actualizado exitosamente", response));
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest()
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMINISTRADOR')")
  public ResponseEntity<ApiResponse<Void>> eliminarCatalogo(@PathVariable UUID id) {
    try {
      catalogoService.eliminarCatalogo(id);
      return ResponseEntity.ok(ApiResponse.success("Catálogo eliminado exitosamente"));
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(ApiResponse.error(e.getMessage()));
    }
  }
}