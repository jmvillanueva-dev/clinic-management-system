package com.clinic.webapi.modules.catalogos.controller;

import com.clinic.webapi.shared.dto.ApiResponse;
import com.clinic.webapi.modules.catalogos.dto.ItemCatalogoRequest;
import com.clinic.webapi.modules.catalogos.dto.ItemCatalogoResponse;
import com.clinic.webapi.modules.catalogos.service.ItemCatalogoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/items-catalogo")
@RequiredArgsConstructor
public class ItemCatalogoController {

  private final ItemCatalogoService itemCatalogoService;

  @PostMapping
  @PreAuthorize("hasRole('ADMINISTRADOR')")
  public ResponseEntity<ApiResponse<ItemCatalogoResponse>> crearItemCatalogo(
      @Valid @RequestBody ItemCatalogoRequest request) {
    try {
      ItemCatalogoResponse response = itemCatalogoService.crearItemCatalogo(request);
      return ResponseEntity.status(HttpStatus.CREATED)
          .body(ApiResponse.success("Ítem de catálogo creado exitosamente", response));
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest()
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  @GetMapping("/catalogo/{catalogoId}")
  @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MEDICO', 'ENFERMERO')")
  public ResponseEntity<ApiResponse<List<ItemCatalogoResponse>>> obtenerItemsPorCatalogo(@PathVariable UUID catalogoId) {
    List<ItemCatalogoResponse> items = itemCatalogoService.obtenerItemsPorCatalogo(catalogoId);
    return ResponseEntity.ok(ApiResponse.success("Ítems de catálogo obtenidos exitosamente", items));
  }

  @GetMapping("/tipo/{tipo}")
  @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MEDICO', 'ENFERMERO')")
  public ResponseEntity<ApiResponse<List<ItemCatalogoResponse>>> obtenerItemsPorTipo(@PathVariable String tipo) {
    List<ItemCatalogoResponse> items = itemCatalogoService.obtenerItemsPorTipo(tipo);
    return ResponseEntity.ok(ApiResponse.success("Ítems de catálogo obtenidos exitosamente", items));
  }

  @GetMapping("/nombre-catalogo/{catalogoNombre}")
  @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MEDICO', 'ENFERMERO')")
  public ResponseEntity<ApiResponse<List<ItemCatalogoResponse>>> obtenerItemsPorNombreCatalogo(@PathVariable String catalogoNombre) {
    List<ItemCatalogoResponse> items = itemCatalogoService.obtenerItemsPorNombreCatalogo(catalogoNombre);
    return ResponseEntity.ok(ApiResponse.success("Ítems de catálogo obtenidos exitosamente", items));
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMINISTRADOR')")
  public ResponseEntity<ApiResponse<ItemCatalogoResponse>> actualizarItemCatalogo(
      @PathVariable UUID id,
      @Valid @RequestBody ItemCatalogoRequest request) {
    try {
      ItemCatalogoResponse response = itemCatalogoService.actualizarItemCatalogo(id, request);
      return ResponseEntity.ok(ApiResponse.success("Ítem de catálogo actualizado exitosamente", response));
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest()
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMINISTRADOR')")
  public ResponseEntity<ApiResponse<Void>> eliminarItemCatalogo(@PathVariable UUID id) {
    try {
      itemCatalogoService.eliminarItemCatalogo(id);
      return ResponseEntity.ok(ApiResponse.success("Ítem de catálogo eliminado exitosamente"));
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  @PatchMapping("/{id}/desactivar")
  @PreAuthorize("hasRole('ADMINISTRADOR')")
  public ResponseEntity<ApiResponse<Void>> desactivarItemCatalogo(@PathVariable UUID id) {
    try {
      itemCatalogoService.desactivarItemCatalogo(id);
      return ResponseEntity.ok(ApiResponse.success("Ítem de catálogo desactivado exitosamente"));
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(ApiResponse.error(e.getMessage()));
    }
  }
}