package com.clinic.webapi.modules.pacientes.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class AntecedenteClinicoRequest {

  @NotNull(message = "El tipo de antecedente es obligatorio")
  private UUID tipoAntecedenteId;

  @NotNull(message = "La patología es obligatoria")
  private UUID patologiaId;

  private String descripcion;

  private LocalDate fechaDiagnostico;

  private String tratamiento;

  private Boolean estaActivo;
}