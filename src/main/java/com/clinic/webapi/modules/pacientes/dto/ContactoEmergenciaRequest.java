package com.clinic.webapi.modules.pacientes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class ContactoEmergenciaRequest {

  @NotBlank(message = "El nombre del contacto es obligatorio")
  private String nombre;

  @NotNull(message = "El parentesco es obligatorio")
  private UUID parentescoId;

  @NotBlank(message = "El teléfono del contacto es obligatorio")
  private String telefono;

  private String direccion;
}