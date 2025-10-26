package com.clinic.webapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class RegisterRequest {

  // Campos para la entidad Empleado
  @NotBlank(message = "El nombre del empleado es obligatorio.")
  @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres.")
  private String nombre;

  @NotBlank(message = "El apellido del empleado es obligatorio.")
  @Size(max = 100, message = "El apellido no puede exceder los 100 caracteres.")
  private String apellido;

  @NotBlank(message = "La cédula es obligatoria.")
  @Size(max = 20, message = "La cédula no puede exceder los 20 caracteres.")
  private String cedula;

  private String especialidad;

  @Size(max = 50, message = "El código profesional no puede exceder los 50 caracteres.")
  private String codigoProfesional;

  private String telefono;

  // Campos para la entidad Usuario
  @NotBlank(message = "El email del usuario es obligatorio.")
  @Email(message = "El formato del email es inválido.")
  private String email;

  // Rol(es) a asignar
  private Set<String> roles;
}