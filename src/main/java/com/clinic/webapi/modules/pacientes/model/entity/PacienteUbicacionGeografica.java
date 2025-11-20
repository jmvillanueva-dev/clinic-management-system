package com.clinic.webapi.modules.pacientes.model.entity;

import com.clinic.webapi.modules.catalogos.model.entity.ItemCatalogo;
import com.clinic.webapi.shared.model.AuditableEntity;
import com.clinic.webapi.shared.util.EntityAuditListener;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "pacientes_ubicacion_geografica")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EntityListeners(EntityAuditListener.class)
public class PacienteUbicacionGeografica implements AuditableEntity {

  @Id
  @UuidGenerator
  private UUID id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "paciente_id", nullable = false, unique = true)
  private Paciente paciente;

  @Column(nullable = false)
  private String direccion;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "provincia_id")
  private ItemCatalogo provincia;

  @Column(length = 100)
  private String canton;

  @Column(length = 100)
  private String parroquia;

  @Column(name = "fecha_creacion", updatable = false)
  private Instant fechaCreacion;

  @Column(name = "fecha_actualizacion")
  private Instant fechaActualizacion;
}
