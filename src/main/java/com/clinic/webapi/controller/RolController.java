package com.clinic.webapi.controller;

import com.clinic.webapi.dto.RolRequest;
import com.clinic.webapi.model.entity.Rol;
import com.clinic.webapi.service.RolService;
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
  public ResponseEntity<Rol> crearRol(@Valid @RequestBody RolRequest request) {
    try {
      Rol nuevoRol = rolService.crearRol(request);
      return new ResponseEntity<>(nuevoRol, HttpStatus.CREATED);
    } catch (RuntimeException e) {
      return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  // GET: Obtener todos los Roles (solo ADMIN)
  @GetMapping
  public ResponseEntity<List<Rol>> obtenerTodosLosRoles() {
    List<Rol> roles = rolService.obtenerTodosLosRoles();
    return ResponseEntity.ok(roles);
  }

  // GET: Obtener un Rol por ID (solo ADMIN)
  @GetMapping("/{id}")
  public ResponseEntity<Rol> obtenerRolPorId(@PathVariable UUID id) {
    try {
      Rol rol = rolService.obtenerRolPorId(id);
      return ResponseEntity.ok(rol);
    } catch (RuntimeException e) {
      return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  // PUT: Actualizar un Rol (solo ADMIN, con excepción)
  @PutMapping("/{id}")
  public ResponseEntity<Rol> actualizarRol(@PathVariable UUID id, @Valid @RequestBody RolRequest request) {
    try {
      Rol rolActualizado = rolService.actualizarRol(id, request);
      return ResponseEntity.ok(rolActualizado);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity(e.getMessage(), HttpStatus.FORBIDDEN);
    } catch (RuntimeException e) {
      return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  // DELETE: Eliminar un Rol (solo ADMIN, con excepción)
  @DeleteMapping("/{id}")
  public ResponseEntity<String> eliminarRol(@PathVariable UUID id) {
    try {
      rolService.eliminarRol(id);
      return ResponseEntity.ok("Rol eliminado correctamente.");
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
    } catch (RuntimeException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
  }
}