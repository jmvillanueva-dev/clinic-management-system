package com.clinic.webapi.modules.evolucionesmedicas.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class EvolucionPlanesTratamientoRequest {
  @NotBlank(message = "El nombre del tratamiento es obligatorio")
  private String nombreTratamiento;

  private String descripcion;
  private String tipoTratamiento;
  private String duracion;
  private List<EvolucionIndicacionesMedicasRequest> indicacionesMedicas;
}