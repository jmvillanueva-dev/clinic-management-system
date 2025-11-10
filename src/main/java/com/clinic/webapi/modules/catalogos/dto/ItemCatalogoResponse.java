package com.clinic.webapi.modules.catalogos.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class ItemCatalogoResponse {
  private UUID id;
  private UUID catalogoId;
  private String catalogoNombre;
  private String nombre;
  private String descripcion;
  private String codigo;
  private String valor;
  private boolean estaActivo;
  private Instant fechaCreacion;
  private Instant fechaActualizacion;
}