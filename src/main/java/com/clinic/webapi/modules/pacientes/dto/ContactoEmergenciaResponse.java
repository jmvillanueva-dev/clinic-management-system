package com.clinic.webapi.modules.pacientes.dto;

import lombok.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactoEmergenciaResponse {
  private UUID id;
  private UUID pacienteId;
  private String nombre;
  private UUID parentescoId;
  private String parentescoNombre;
  private String telefono;
  private String direccion;
}
