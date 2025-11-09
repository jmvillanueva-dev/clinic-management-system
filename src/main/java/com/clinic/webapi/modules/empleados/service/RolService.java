package com.clinic.webapi.modules.empleados.service;

import com.clinic.webapi.modules.empleados.dto.RolRequest;
import com.clinic.webapi.modules.empleados.entity.Rol;
import com.clinic.webapi.modules.empleados.repository.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RolService {

  private final RolRepository rolRepository;

  private static final String ADMIN_ROLE_NAME = "ADMINISTRADOR";

  // --- C: Crear Rol ---
  public Rol crearRol(RolRequest request) {
    if (rolRepository.existsByNombre(request.getNombre())) {
      throw new RuntimeException("El rol con nombre '" + request.getNombre() + "' ya existe.");
    }

     if (request.getNombre().toUpperCase().equals(ADMIN_ROLE_NAME)) {
         throw new RuntimeException("No se puede crear manualmente el rol ADMINISTRADOR.");
     }

    Rol rol = Rol.builder()
        .nombre(request.getNombre())
        .area(request.getArea())
        .descripcion(request.getDescripcion())
        .build();

    return rolRepository.save(rol);
  }

  // --- R: Leer Roles ---
  public List<Rol> obtenerTodosLosRoles() {
    return rolRepository.findAll();
  }

  public Rol obtenerRolPorId(UUID id) {
    return rolRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + id));
  }

  // --- U: Actualizar Rol ---
  public Rol actualizarRol(UUID id, RolRequest request) {
    Rol rolExistente = obtenerRolPorId(id);

    // 1. Restricción: No se puede modificar el rol ADMINISTRADOR
    if (rolExistente.getNombre().toUpperCase().equals(ADMIN_ROLE_NAME)) {
      throw new IllegalArgumentException("No está permitido modificar el rol '" + ADMIN_ROLE_NAME + "'.");
    }

    // 2. Restricción: Validar el nuevo nombre (si cambió)
    if (request.getNombre() != null && !request.getNombre().equalsIgnoreCase(rolExistente.getNombre())) {
      if (request.getNombre().toUpperCase().equals(ADMIN_ROLE_NAME)) {
        throw new IllegalArgumentException("El rol '" + ADMIN_ROLE_NAME + "' está reservado y no puede ser asignado.");
      }
      if (rolRepository.existsByNombre(request.getNombre())) {
        throw new RuntimeException("El rol con nombre '" + request.getNombre() + "' ya existe.");
      }
      rolExistente.setNombre(request.getNombre());
    }

    // 3. Actualizar campos
    rolExistente.setArea(request.getArea() != null ? request.getArea() : rolExistente.getArea());
    rolExistente.setDescripcion(request.getDescripcion() != null ? request.getDescripcion() : rolExistente.getDescripcion());

    return rolRepository.save(rolExistente);
  }

  // --- D: Eliminar Rol ---
  public void eliminarRol(UUID id) {
    Rol rolExistente = obtenerRolPorId(id);

    // Restricción: No se puede eliminar el rol ADMINISTRADOR
    if (rolExistente.getNombre().toUpperCase().equals(ADMIN_ROLE_NAME)) {
      throw new IllegalArgumentException("No está permitido eliminar el rol '" + ADMIN_ROLE_NAME + "'.");
    }

    rolRepository.delete(rolExistente);
  }
}