package com.clinic.webapi.modules.auth.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class UsuarioResponse {
  private UUID id;
  private String email;
  private boolean estaActivo;
  private boolean estaVerificado;
  private Instant fechaActualizacion;
  private Set<String> roles;
  private UUID idEmpleado;
}