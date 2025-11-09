package com.clinic.webapi.modules.auth.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class RegisterResponse {
  private UUID idUsuario;
  private String email;
  private boolean estaActivo;
  private boolean estaVerificado;
  private Instant fechaCreacion;
  private Set<String> roles;

  private UUID idEmpleado;
  private String nombre;
  private String apellido;
  private String cedula;
  private String especialidad;
}