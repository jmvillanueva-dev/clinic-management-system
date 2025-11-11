package com.clinic.webapi.modules.evolucionesmedicas.entity;

import com.clinic.webapi.shared.util.EntityAuditListener;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "evolucion_planes_tratamiento")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EntityListeners(EntityAuditListener.class)
public class EvolucionPlanesTratamiento {

  @Id
  @UuidGenerator
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "evolucion_medica_id", nullable = false)
  private EvolucionMedica evolucionMedica;

  @Column(name = "nombre_tratamiento", nullable = false, length = 200)
  private String nombreTratamiento;

  @Column(name = "descripcion", columnDefinition = "TEXT")
  private String descripcion;

  @Column(name = "tipo_tratamiento", length = 100)
  private String tipoTratamiento; // Farmacológico, Quirúrgico, Rehabilitación, etc.

  @Column(name = "duracion", length = 100)
  private String duracion; // "7 días", "1 mes", etc.

  @Column(name = "fecha_creacion", updatable = false)
  private Instant fechaCreacion;

  @OneToMany(mappedBy = "planTratamiento", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @Builder.Default
  private List<EvolucionIndicacionesMedicas> indicacionesMedicas = new ArrayList<>();
}