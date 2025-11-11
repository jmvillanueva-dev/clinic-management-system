package com.clinic.webapi.modules.evolucionesmedicas.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EvolucionLocalizacionLesionesRequest {
  @NotBlank(message = "La localización es obligatoria")
  private String localizacion;

  private String tipoLesion;
  private String descripcion;
  private String gravedad;
}