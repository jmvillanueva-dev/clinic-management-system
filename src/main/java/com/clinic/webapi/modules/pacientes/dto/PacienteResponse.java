package com.clinic.webapi.modules.pacientes.dto;

import com.clinic.webapi.modules.catalogos.dto.ItemCatalogoResponse;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class PacienteResponse {

  private UUID id;
  private String cedula;
  private String primerNombre;
  private String segundoNombre;
  private String apellidoPaterno;
  private String apellidoMaterno;
  private String email;
  private String telefono;
  private ItemCatalogoResponse grupoSanguineo;
  private Instant fechaCreacion;
  private Instant fechaActualizacion;
  private boolean estaActivo;

  // Datos demográficos
  private LocalDate fechaNacimiento;
  private String lugarNacimiento;
  private ItemCatalogoResponse genero;
  private String nacionalidad;
  private ItemCatalogoResponse grupoCultural;
  private ItemCatalogoResponse estadoCivil;
  private ItemCatalogoResponse nivelInstruccion;

  // Ubicación
  private String direccion;
  private ItemCatalogoResponse provincia;
  private String canton;
  private String parroquia;

  // Ocupación
  private OcupacionResponse ocupacion;

  // Fuente de información
  private FuenteInformacionResponse fuenteInformacion;

  // Contactos de emergencia
  private Set<ContactoEmergenciaResponse> contactosEmergencia;

  // Antecedentes clínicos
  private Set<AntecedenteClinicoResponse> antecedentesClinicos;
}