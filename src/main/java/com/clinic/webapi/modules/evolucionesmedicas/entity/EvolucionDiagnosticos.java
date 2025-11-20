package com.clinic.webapi.modules.evolucionesmedicas.entity;

import com.clinic.webapi.shared.model.AuditableEntity;
import com.clinic.webapi.shared.util.EntityAuditListener;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "evolucion_diagnosticos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EntityListeners(EntityAuditListener.class)
public class EvolucionDiagnosticos implements AuditableEntity {

  @Id
  @UuidGenerator
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "evolucion_medica_id", nullable = false)
  private EvolucionMedica evolucionMedica;

  @Column(name = "codigo_cie", length = 20)
  private String codigoCie; // Código CIE-10

  @Column(name = "diagnostico", nullable = false, length = 500)
  private String diagnostico;

  @Column(name = "tipo", length = 50)
  private String tipo; // Presuntivo, Definitivo

  @Column(name = "observaciones", columnDefinition = "TEXT")
  private String observaciones;

  @Column(name = "fecha_creacion", updatable = false)
  private Instant fechaCreacion;

  @Column(name = "fecha_actualizacion")
  private Instant fechaActualizacion;
}
