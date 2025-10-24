package com.clinic.webapi.model.entity;

import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.Set;
import java.util.HashSet;
import java.util.UUID;


@Entity
@Table(name = "usuarios")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Usuario {

  @Id
  @UuidGenerator
  private UUID id;

  @Column(nullable = false, unique = true, name = "email")
  private String email;

  @Column(nullable = false, name = "password_hash")
  private String passwordHash;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "empleado_id", nullable = false)
  private Empleado empleado;

  @Column(name = "token_verificacion")
  private String tokenVerificacion;

  @Column(name = "esta_activo", nullable = false)
  private boolean estaActivo = true;

  @Column(name = "esta_verificado", nullable = false)
  private boolean estaVerificado = false;

  @Column(name = "fecha_creacion", updatable = false)
  private Instant fechaCreacion = Instant.now();

  @Column(name = "fecha_actualizacion")
  private Instant fechaActualizacion = Instant.now();

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "usuario_rol",
      joinColumns = @JoinColumn(name="usuario_id"),
      inverseJoinColumns = @JoinColumn(name="rol_id")
  )
  private Set<Rol> roles = new HashSet<>();
}