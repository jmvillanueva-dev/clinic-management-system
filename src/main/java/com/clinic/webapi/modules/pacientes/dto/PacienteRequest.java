package com.clinic.webapi.modules.pacientes.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class PacienteRequest {

  // Datos básicos
  @NotBlank(message = "La cédula es obligatoria")
  private String cedula;

  @NotBlank(message = "El primer nombre es obligatorio")
  private String primerNombre;

  private String segundoNombre;

  @NotBlank(message = "El apellido paterno es obligatorio")
  private String apellidoPaterno;

  private String apellidoMaterno;

  @Email(message = "El formato del email es inválido")
  private String email;

  private String telefono;

  private UUID grupoSanguineoId;

  // Datos demográficos
  @NotNull(message = "La fecha de nacimiento es obligatoria")
  private LocalDate fechaNacimiento;

  private String lugarNacimiento;

  @NotNull(message = "El género es obligatorio")
  private UUID generoId;

  private String nacionalidad;

  private UUID grupoCulturalId;

  private UUID estadoCivilId;

  private UUID nivelInstruccionId;

  // Ubicación
  private String direccion;

  private UUID provinciaId;

  private String canton;

  private String parroquia;

  // Ocupación
  private UUID ocupacionId;

  private String nombreEmpresa;

  private String cargo;

  private String telefonoEmpresa;

  private String direccionEmpresa;

  private LocalDate fechaInicio;

  private LocalDate fechaFin;

  private Boolean actual;

  // Fuente de información
  private UUID fuenteInformacionId;

  private String nombreFuenteInfo;

  private String telefonoFuenteInfo;

  private String observacionesFuente;

  // Contactos de emergencia
  private List<ContactoEmergenciaRequest> contactosEmergencia;

  // Antecedentes clínicos
  private List<AntecedenteClinicoRequest> antecedentesClinicos;
}