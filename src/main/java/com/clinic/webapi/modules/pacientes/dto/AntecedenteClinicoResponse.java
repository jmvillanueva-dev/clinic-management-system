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
public class AntecedenteClinicoResponse {
  private UUID id;
  private UUID pacienteId;
  private UUID tipoAntecedenteId;
  private String tipoAntecedenteNombre;
  private UUID patologiaId;
  private String patologiaNombre;
  private String descripcion;
  private LocalDate fechaDiagnostico;
  private String tratamiento;
  private boolean estaActivo;
  private Instant fechaCreacion;
}
