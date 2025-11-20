package com.clinic.webapi.modules.evolucionesmedicas.entity;

import com.clinic.webapi.shared.model.AuditableEntity;
import com.clinic.webapi.shared.util.EntityAuditListener;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "evolucion_examenes_solicitados")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EntityListeners(EntityAuditListener.class)
public class EvolucionExamenesSolicitados implements AuditableEntity {

  @Id
  @UuidGenerator
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "evolucion_medica_id", nullable = false)
  private EvolucionMedica evolucionMedica;

  @Column(name = "tipo_examen", nullable = false, length = 200)
  private String tipoExamen; // Laboratorio, Rayos X, Ecografía, etc.

  @Column(name = "nombre_examen", nullable = false, length = 200)
  private String nombreExamen;

  @Column(name = "indicaciones", columnDefinition = "TEXT")
  private String indicaciones;

  @Column(name = "urgencia", length = 50)
  @Builder.Default
  private String urgencia = "ROUTINA"; // RUTINA, URGENTE

  @Column(name = "estado", length = 50)
  @Builder.Default
  private String estado = "SOLICITADO"; // SOLICITADO, REALIZADO, CANCELADO

  @Column(name = "fecha_creacion", updatable = false)
  private Instant fechaCreacion;

  @Column(name = "fecha_actualizacion")
  private Instant fechaActualizacion;
}
