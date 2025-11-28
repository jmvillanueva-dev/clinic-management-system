package com.clinic.webapi.modules.catalogos.dto;

import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data
@Builder
public class ItemCatalogoMinifiedResponse {
  private UUID id;
  private UUID catalogoId;
  private String catalogoNombre;
  private String nombre;
  private String codigo;
  private String valor;
}
