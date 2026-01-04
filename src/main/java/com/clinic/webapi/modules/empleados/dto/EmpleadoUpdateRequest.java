package com.clinic.webapi.modules.empleados.dto;

import lombok.Data;
import jakarta.validation.constraints.Size;
import java.util.Set;

@Data
public class EmpleadoUpdateRequest {

  @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres.")
  private String nombre;

  @Size(max = 100, message = "El apellido no puede exceder los 100 caracteres.")
  private String apellido;

  @Size(max = 20, message = "La cédula no puede exceder los 20 caracteres.")
  private String cedula;

  private String especialidad;

  @Size(max = 50, message = "El código profesional no puede exceder los 50 caracteres.")
  private String codigoProfesional;

  private String telefono;

  private Set<String> roles;
}