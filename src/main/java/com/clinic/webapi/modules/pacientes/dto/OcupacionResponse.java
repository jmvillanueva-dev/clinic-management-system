package com.clinic.webapi.modules.pacientes.dto;

import lombok.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OcupacionResponse {
  private UUID id;
  private UUID pacienteId;
  private UUID ocupacionId;
  private String ocupacionNombre;
  private String nombreEmpresa;
  private String cargo;
  private String telefonoEmpresa;
  private String direccionEmpresa;
  private LocalDate fechaInicio;
  private LocalDate fechaFin;
  private boolean actual;
  private Instant fechaCreacion;
  private Instant fechaActualizacion;
}
