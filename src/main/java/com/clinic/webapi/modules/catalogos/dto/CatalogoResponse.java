package com.clinic.webapi.modules.catalogos.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class CatalogoResponse {
  private UUID id;
  private String nombre;
  private String descripcion;
  private String tipo;
  private boolean estaActivo;
  private Instant fechaCreacion;
  private Instant fechaActualizacion;
  private List<ItemCatalogoResponse> items;
}