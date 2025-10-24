package com.clinic.webapi.dto;

import lombok.Builder;
import lombok.Data;
import java.util.UUID;
import java.time.Instant;

@Data
@Builder
public class EmpleadoResponse {
  private UUID id;
  private String nombre;
  private String apellido;
  private String cedula;
  private String especialidad;
  private String codigoProfesional;
  private String telefono;
  private boolean estaActivo;
  private Instant fechaCreacion;
}