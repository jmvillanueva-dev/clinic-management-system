package com.clinic.webapi.modules.evolucionesmedicas.entity;

import com.clinic.webapi.shared.model.AuditableEntity;
import com.clinic.webapi.shared.util.EntityAuditListener;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "evolucion_indicaciones_medicas")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EntityListeners(EntityAuditListener.class)
public class EvolucionIndicacionesMedicas implements AuditableEntity {

  @Id
  @UuidGenerator
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "plan_tratamiento_id", nullable = false)
  private EvolucionPlanesTratamiento planTratamiento;

  @Column(name = "medicamento", length = 200)
  private String medicamento;

  @Column(name = "dosis", length = 100)
  private String dosis;

  @Column(name = "frecuencia", length = 100)
  private String frecuencia;

  @Column(name = "via_administracion", length = 100)
  private String viaAdministracion;

  @Column(name = "duracion", length = 100)
  private String duracion;

  @Column(name = "indicaciones_especiales", columnDefinition = "TEXT")
  private String indicacionesEspeciales;

  @Column(name = "fecha_creacion", updatable = false)
  private Instant fechaCreacion;

  @Column(name = "fecha_actualizacion")
  private Instant fechaActualizacion;
}
