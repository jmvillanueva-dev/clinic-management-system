package com.clinic.webapi.modules.pacientes.dto;

import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FuenteInformacionResponse {
  private UUID id;
  private UUID pacienteId;
  private UUID fuenteInformacionId;
  private String fuenteInformacionNombre;
  private String nombreFuenteInfo;
  private String telefono;
  private String observaciones;
  private Instant fechaCreacion;
  private Instant fechaActualizacion;
}
