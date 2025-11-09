package com.clinic.webapi.modules.empleados.controller;

import com.clinic.webapi.modules.empleados.dto.RolRequest;
import com.clinic.webapi.modules.empleados.entity.Rol;
import com.clinic.webapi.modules.empleados.service.RolService;
import com.clinic.webapi.shared.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class RolController {

  private final RolService rolService;

  // POST: Crear un nuevo Rol (solo ADMIN)
  @PostMapping
  public ResponseEntity<ApiResponse<Rol>> crearRol(@Valid @RequestBody RolRequest request) {
    try {
      Rol nuevoRol = rolService.crearRol(request);
      return ResponseEntity.ok(ApiResponse.success("Rol creado exitosamente", nuevoRol));
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest()
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  // GET: Obtener todos los Roles (solo ADMIN)
  @GetMapping
  public ResponseEntity<ApiResponse<List<Rol>>> obtenerTodosLosRoles() {
    List<Rol> roles = rolService.obtenerTodosLosRoles();
    return ResponseEntity.ok(ApiResponse.success("Roles obtenidos exitosamente", roles));
  }

  // GET: Obtener un Rol por ID (solo ADMIN)
  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<Rol>> obtenerRolPorId(@PathVariable UUID id) {
    try {
      Rol rol = rolService.obtenerRolPorId(id);
      return ResponseEntity.ok(ApiResponse.success("Rol obtenido exitosamente", rol));
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  // PUT: Actualizar un Rol (solo ADMIN, con excepción)
  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse<Rol>> actualizarRol(@PathVariable UUID id, @Valid @RequestBody RolRequest request) {
    try {
      Rol rolActualizado = rolService.actualizarRol(id, request);
      return ResponseEntity.ok(ApiResponse.success("Rol actualizado exitosamente", rolActualizado));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN)
          .body(ApiResponse.error(e.getMessage()));
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  // DELETE: Eliminar un Rol (solo ADMIN, con excepción)
  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<Void>> eliminarRol(@PathVariable UUID id) {
    try {
      rolService.eliminarRol(id);
      return ResponseEntity.ok(ApiResponse.success("Rol eliminado correctamente"));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN)
          .body(ApiResponse.error(e.getMessage()));
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(ApiResponse.error(e.getMessage()));
    }
  }
}