package com.clinic.webapi.modules.evolucionesmedicas.entity;

import com.clinic.webapi.shared.model.AuditableEntity;
import com.clinic.webapi.shared.util.EntityAuditListener;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "evolucion_motivo_atencion")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EntityListeners(EntityAuditListener.class)
public class EvolucionMotivoAtencion implements AuditableEntity {

  @Id
  @UuidGenerator
  private UUID id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "evolucion_medica_id", nullable = false, unique = true)
  private EvolucionMedica evolucionMedica;

  @Column(name = "motivo_consulta", nullable = false, columnDefinition = "TEXT")
  private String motivoConsulta;

  @Column(name = "enfermedad_actual", columnDefinition = "TEXT")
  private String enfermedadActual;

  @Column(name = "fecha_creacion", updatable = false)
  private Instant fechaCreacion;

  @Column(name = "fecha_actualizacion")
  private Instant fechaActualizacion;
}
