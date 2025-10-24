package com.clinic.webapi.controller;

import com.clinic.webapi.dto.EmpleadoUpdateRequest;
import com.clinic.webapi.dto.UsuarioEmailUpdateRequest;
import com.clinic.webapi.dto.UsuarioPasswordUpdateRequest;
import com.clinic.webapi.dto.EmpleadoResponse;
import com.clinic.webapi.model.entity.Empleado;
import com.clinic.webapi.model.entity.Usuario;
import com.clinic.webapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/empleados")
@RequiredArgsConstructor
public class EmpleadoController {

  private final UserService userService;

  // --- 1. CRUD Empleados (ADMIN: CRUD; Empleado: R, U propio) ---

  // Obtener un empleado por ID (Cualquier empleado puede verlo, ADMIN: CRUD)
  @GetMapping("/{id}")
  @PreAuthorize("@userService.canAccessEmpleadoById(principal, #id)")
  public ResponseEntity<EmpleadoResponse> obtenerEmpleadoPorId(@PathVariable UUID id) {
    try {
      Empleado empleado = userService.findEmpleadoById(id);

      // Mapear Empleado a EmpleadoResponse
      EmpleadoResponse response = EmpleadoResponse.builder()
          .id(empleado.getId())
          .nombre(empleado.getNombre())
          .apellido(empleado.getApellido())
          .cedula(empleado.getCedula())
          .especialidad(empleado.getEspecialidad())
          .codigoProfesional(empleado.getCodigoProfesional())
          .telefono(empleado.getTelefono())
          .estaActivo(empleado.isEstaActivo())
          .fechaCreacion(empleado.getFechaCreacion())
          .build();

      return ResponseEntity.ok(response);
    } catch (RuntimeException e) {
      return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  // Actualizar datos de Empleado (ADMIN: cualquiera; Empleado: solo el suyo)
  @PutMapping("/{id}")
  @PreAuthorize("@userService.canAccessEmpleadoById(principal, #id)")
  public ResponseEntity<Empleado> actualizarEmpleado(@PathVariable UUID id,
                                                     @Valid @RequestBody EmpleadoUpdateRequest request,
                                                     @AuthenticationPrincipal String userIdString) {

    UUID userId = UUID.fromString(userIdString);

    try {
      Empleado empleadoActualizado = userService.actualizarEmpleado(id, request, userId);
      return ResponseEntity.ok(empleadoActualizado);
    } catch (SecurityException e) {
      return new ResponseEntity(e.getMessage(), HttpStatus.FORBIDDEN);
    } catch (RuntimeException e) {
      return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  // "Eliminar" (Desactivar) Empleado (Solo ADMIN)
  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMINISTRADOR')")
  public ResponseEntity<Void> eliminarEmpleado(@PathVariable UUID id, @AuthenticationPrincipal String userEmail) {
    try {
      // Buscar el usuario por email para obtener el UUID
      UUID userId = userService.findByEmail(userEmail)
          .orElseThrow(() -> new RuntimeException("Usuario autenticado no encontrado."))
          .getId();

      userService.eliminarEmpleado(id, userId);
      return ResponseEntity.noContent().build();
    } catch (SecurityException e) {
      return new ResponseEntity(e.getMessage(), HttpStatus.FORBIDDEN);
    } catch (RuntimeException e) {
      return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  // --- 2. Actualización de credenciales de Usuario ---

  // Actualizar Email de Usuario (ADMIN: cualquiera; Empleado: solo el suyo)
  @PutMapping("/{id}/email")
  @PreAuthorize("@userService.canAccessEmpleadoById(principal, #id)")
  public ResponseEntity<Usuario> actualizarEmailUsuario(@PathVariable UUID id,
                                                        @Valid @RequestBody UsuarioEmailUpdateRequest request,
                                                        @AuthenticationPrincipal String userIdString) {
    UUID userId = UUID.fromString(userIdString);

    try {
      Usuario usuarioActualizado = userService.actualizarUsuarioEmail(id, request, userId);
      return ResponseEntity.ok(usuarioActualizado);
    } catch (SecurityException e) {
      return new ResponseEntity(e.getMessage(), HttpStatus.FORBIDDEN);
    } catch (RuntimeException e) {
      return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  // Actualizar Contraseña del Usuario logueado (Solo Empleado logueado)
  @PutMapping("/me/password")
  public ResponseEntity<Usuario> actualizarPasswordUsuario(@Valid @RequestBody UsuarioPasswordUpdateRequest request,
                                                           @AuthenticationPrincipal String userIdString) {
    UUID userId = UUID.fromString(userIdString);

    try {
      Usuario usuarioActualizado = userService.actualizarUsuarioPassword(request, userId);
      return ResponseEntity.ok(usuarioActualizado);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
    } catch (RuntimeException e) {
      return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}