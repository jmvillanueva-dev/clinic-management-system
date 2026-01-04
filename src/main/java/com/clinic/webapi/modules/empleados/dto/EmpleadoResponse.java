package com.clinic.webapi.modules.empleados.dto;

import lombok.Builder;
import lombok.Data;
import java.util.UUID;
import java.time.Instant;
import java.util.Set;

@Data
@Builder
public class EmpleadoResponse {
  private UUID id;
  private String nombre;
  private String apellido;
  private String email;
  private String cedula;
  private String especialidad;
  private String codigoProfesional;
  private String telefono;
  private boolean estaActivo;
  private Instant fechaCreacion;
  private Set<String> roles;
}