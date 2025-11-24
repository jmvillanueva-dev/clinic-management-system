package com.clinic.webapi.modules.auth.entity;

import com.clinic.webapi.shared.model.AuditableEntity;
import com.clinic.webapi.shared.util.EntityAuditListener;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "refresh_tokens")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EntityListeners(EntityAuditListener.class)
public class RefreshToken implements AuditableEntity {

  @Id
  @UuidGenerator
  private UUID id;

  @Column(nullable = false, unique = true)
  private String token;

  @Column(nullable = false)
  private Instant expiryDate;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "usuario_id", referencedColumnName = "id")
  private Usuario usuario;

  @Column(name = "fecha_creacion", updatable = false)
  private Instant fechaCreacion;

  @Column(name = "fecha_actualizacion")
  private Instant fechaActualizacion;
}