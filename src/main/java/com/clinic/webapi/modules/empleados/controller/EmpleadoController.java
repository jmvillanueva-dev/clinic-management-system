package com.clinic.webapi.modules.empleados.controller;

import com.clinic.webapi.shared.dto.ApiResponse;
import com.clinic.webapi.modules.empleados.dto.EmpleadoUpdateRequest;
import com.clinic.webapi.modules.auth.dto.UsuarioEmailUpdateRequest;
import com.clinic.webapi.modules.auth.dto.UsuarioPasswordUpdateRequest;
import com.clinic.webapi.modules.auth.dto.UsuarioResponse;
import com.clinic.webapi.modules.empleados.dto.EmpleadoResponse;
import com.clinic.webapi.modules.empleados.entity.Empleado;
import com.clinic.webapi.modules.empleados.entity.Rol;
import com.clinic.webapi.modules.auth.entity.Usuario;
import com.clinic.webapi.modules.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/empleados")
@RequiredArgsConstructor
public class EmpleadoController {

  private final UserService userService;

  // Método auxiliar para mapear Usuario a UsuarioResponse
  private UsuarioResponse mapToUsuarioResponse(Usuario usuario) {
    Set<String> roles = usuario.getRoles().stream()
        .map(rol -> rol.getNombre())
        .collect(Collectors.toSet());

    return UsuarioResponse.builder()
        .id(usuario.getId())
        .email(usuario.getEmail())
        .estaActivo(usuario.isEstaActivo())
        .estaVerificado(usuario.isEstaVerificado())
        .fechaActualizacion(usuario.getFechaActualizacion())
        .roles(roles)
        .idEmpleado(usuario.getEmpleado() != null ? usuario.getEmpleado().getId() : null)
        .build();
  }

  // --- 1. CRUD Empleados (ADMIN: CRUD; Empleado: R, U propio) ---

  // Obtener todos los empleados activos (Solo ADMIN)
  @GetMapping
  @PreAuthorize("hasRole('ADMINISTRADOR')")
  public ResponseEntity<ApiResponse<List<EmpleadoResponse>>> obtenerTodosLosEmpleadosActivos() {
    List<Empleado> empleados = userService.findAllActiveEmpleados();
    List<EmpleadoResponse> responses = empleados.stream()
        .map(empleado -> {
          Optional<Usuario> usuarioOpt = userService.findByEmpleado(empleado);
          String email = usuarioOpt.map(Usuario::getEmail).orElse(null);
          Set<String> roles = usuarioOpt.map(u -> u.getRoles().stream()
              .map(Rol::getNombre)
              .collect(Collectors.toSet()))
              .orElse(Set.of());

          return EmpleadoResponse.builder()
              .id(empleado.getId())
              .nombre(empleado.getNombre())
              .apellido(empleado.getApellido())
              .email(email)
              .cedula(empleado.getCedula())
              .especialidad(empleado.getEspecialidad())
              .codigoProfesional(empleado.getCodigoProfesional())
              .telefono(empleado.getTelefono())
              .estaActivo(empleado.isEstaActivo())
              .fechaCreacion(empleado.getFechaCreacion())
              .roles(roles)
              .build();
        })
        .collect(Collectors.toList());
    return ResponseEntity.ok(ApiResponse.success("Empleados obtenidos exitosamente", responses));
  }

  // Obtener un empleado por ID (Cualquier empleado puede verlo, ADMIN: CRUD)
  @GetMapping("/{id}")
  @PreAuthorize("@userService.canAccessEmpleadoById(principal, #id)")
  public ResponseEntity<ApiResponse<EmpleadoResponse>> obtenerEmpleadoPorId(@PathVariable UUID id) {
    try {
      Empleado empleado = userService.findEmpleadoById(id);
      Optional<Usuario> usuarioOpt = userService.findByEmpleado(empleado);
      String email = usuarioOpt.map(Usuario::getEmail).orElse(null);
      Set<String> roles = usuarioOpt.map(u -> u.getRoles().stream()
              .map(Rol::getNombre)
              .collect(Collectors.toSet()))
              .orElse(Set.of());

      EmpleadoResponse response = EmpleadoResponse.builder()
          .id(empleado.getId())
          .nombre(empleado.getNombre())
          .apellido(empleado.getApellido())
          .email(email)
          .cedula(empleado.getCedula())
          .especialidad(empleado.getEspecialidad())
          .codigoProfesional(empleado.getCodigoProfesional())
          .telefono(empleado.getTelefono())
          .estaActivo(empleado.isEstaActivo())
          .fechaCreacion(empleado.getFechaCreacion())
          .roles(roles)
          .build();

      return ResponseEntity.ok(ApiResponse.success("Empleado obtenido exitosamente", response));
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  // Actualizar datos de Empleado (ADMIN: cualquiera; Empleado: solo el suyo)
  @PutMapping("/{id}")
  @PreAuthorize("@userService.canAccessEmpleadoById(principal, #id)")
  public ResponseEntity<ApiResponse<EmpleadoResponse>> actualizarEmpleado(@PathVariable UUID id,
                                                                          @Valid @RequestBody EmpleadoUpdateRequest request,
                                                                          @AuthenticationPrincipal String userIdString) {

    UUID userId = UUID.fromString(userIdString);

    try {
      Empleado empleadoActualizado = userService.actualizarEmpleado(id, request, userId);
      Optional<Usuario> usuarioOpt = userService.findByEmpleado(empleadoActualizado);
      String email = usuarioOpt.map(Usuario::getEmail).orElse(null);
      Set<String> roles = usuarioOpt.map(u -> u.getRoles().stream()
              .map(Rol::getNombre)
              .collect(Collectors.toSet()))
              .orElse(Set.of());

      EmpleadoResponse response = EmpleadoResponse.builder()
          .id(empleadoActualizado.getId())
          .nombre(empleadoActualizado.getNombre())
          .apellido(empleadoActualizado.getApellido())
          .email(email)
          .cedula(empleadoActualizado.getCedula())
          .especialidad(empleadoActualizado.getEspecialidad())
          .codigoProfesional(empleadoActualizado.getCodigoProfesional())
          .telefono(empleadoActualizado.getTelefono())
          .estaActivo(empleadoActualizado.isEstaActivo())
          .fechaCreacion(empleadoActualizado.getFechaCreacion())
          .roles(roles)
          .build();

      return ResponseEntity.ok(ApiResponse.success("Empleado actualizado exitosamente", response));
    } catch (SecurityException e) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN)
          .body(ApiResponse.error(e.getMessage()));
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  // "Eliminar" (Desactivar) Empleado (Solo ADMIN)
  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMINISTRADOR')")
  public ResponseEntity<ApiResponse<Void>> eliminarEmpleado(@PathVariable UUID id, @AuthenticationPrincipal String userIdString) {
    try {
      UUID userId = UUID.fromString(userIdString);
      userService.eliminarEmpleado(id, userId);

      return ResponseEntity.ok(ApiResponse.success("Empleado desactivado exitosamente"));

    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(ApiResponse.error("Token de autenticación inválido"));
    } catch (SecurityException e) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN)
          .body(ApiResponse.error(e.getMessage()));
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  // --- 2. Actualización de credenciales de Usuario ---

  // Actualizar Email de Usuario (ADMIN: cualquiera; Empleado: solo el suyo)
  @PutMapping("/{id}/email")
  @PreAuthorize("@userService.canAccessEmpleadoById(principal, #id)")
  public ResponseEntity<ApiResponse<UsuarioResponse>> actualizarEmailUsuario(@PathVariable UUID id,
                                                                             @Valid @RequestBody UsuarioEmailUpdateRequest request,
                                                                             @AuthenticationPrincipal String userIdString) {
    UUID userId = UUID.fromString(userIdString);

    try {
      Usuario usuarioActualizado = userService.actualizarUsuarioEmail(id, request, userId);
      return ResponseEntity.ok(ApiResponse.success("Email actualizado exitosamente", mapToUsuarioResponse(usuarioActualizado)));
    } catch (SecurityException e) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN)
          .body(ApiResponse.error(e.getMessage()));
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  // Actualizar Contraseña del Usuario logueado (Solo Empleado logueado)
  @PutMapping("/me/password")
  public ResponseEntity<ApiResponse<UsuarioResponse>> actualizarPasswordUsuario(@Valid @RequestBody UsuarioPasswordUpdateRequest request,
                                                                                @AuthenticationPrincipal String userIdString) {
    UUID userId = UUID.fromString(userIdString);

    try {
      Usuario usuarioActualizado = userService.actualizarUsuarioPassword(request, userId);
      return ResponseEntity.ok(ApiResponse.success("Contraseña actualizada exitosamente", mapToUsuarioResponse(usuarioActualizado)));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(ApiResponse.error(e.getMessage()));
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(ApiResponse.error(e.getMessage()));
    }
  }
}