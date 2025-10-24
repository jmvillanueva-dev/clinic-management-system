package com.clinic.webapi.model.entity;

import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "empleados")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Empleado {

  @Id
  @UuidGenerator
  private UUID id;

  @Column(nullable = false, length = 100)
  private String nombre;

  @Column(nullable = false, length = 100)
  private String apellido;

  @Column(nullable = false, unique = true, length = 20)
  private String cedula;

  private String especialidad;

  @Column(name = "codigo_profesional", length = 50)
  private String codigoProfesional;

  private String telefono;

  @Column(name = "esta_activo", nullable = false)
  private boolean estaActivo = true;

  @Column(name = "fecha_creacion", updatable = false)
  private Instant fechaCreacion = Instant.now();

  @Column(name = "fecha_actualizacion")
  private Instant fechaActualizacion = Instant.now();

}