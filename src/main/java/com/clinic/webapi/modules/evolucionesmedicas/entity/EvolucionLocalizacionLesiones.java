package com.clinic.webapi.modules.evolucionesmedicas.entity;

import com.clinic.webapi.shared.model.AuditableEntity;
import com.clinic.webapi.shared.util.EntityAuditListener;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "evolucion_localizacion_lesiones")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EntityListeners(EntityAuditListener.class)
public class EvolucionLocalizacionLesiones implements AuditableEntity {

  @Id
  @UuidGenerator
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "evolucion_medica_id", nullable = false)
  private EvolucionMedica evolucionMedica;

  @Column(name = "localizacion", nullable = false, length = 200)
  private String localizacion; // Ej: "Brazo derecho", "Pierna izquierda"

  @Column(name = "tipo_lesion", length = 100)
  private String tipoLesion; // Herida, contusión, fractura, etc.

  @Column(name = "descripcion", columnDefinition = "TEXT")
  private String descripcion;

  @Column(name = "gravedad", length = 50)
  private String gravedad; // Leve, Moderada, Grave

  @Column(name = "fecha_creacion", updatable = false)
  private Instant fechaCreacion;

  @Column(name = "fecha_actualizacion")
  private Instant fechaActualizacion;
}
