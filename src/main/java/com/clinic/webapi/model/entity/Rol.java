package com.clinic.webapi.model.entity;

import com.clinic.webapi.util.EntityAuditListener;
import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "roles")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EntityListeners(EntityAuditListener.class)
public class Rol {

  @Id
  @UuidGenerator
  private UUID id;

  @Column(nullable = false, unique = true, length = 50)
  private String nombre;

  @Column(nullable = false, length = 100)
  private String area;

  private String descripcion;

  @Column(name = "fecha_creacion", updatable = false)
  private Instant fechaCreacion;

  @Column(name = "fecha_actualizacion")
  private Instant fechaActualizacion;
}