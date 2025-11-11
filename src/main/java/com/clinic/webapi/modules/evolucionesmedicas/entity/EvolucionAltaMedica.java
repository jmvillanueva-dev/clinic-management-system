package com.clinic.webapi.modules.evolucionesmedicas.entity;

import com.clinic.webapi.shared.util.EntityAuditListener;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "evolucion_alta_medica")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EntityListeners(EntityAuditListener.class)
public class EvolucionAltaMedica {

  @Id
  @UuidGenerator
  private UUID id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "evolucion_medica_id", nullable = false, unique = true)
  private EvolucionMedica evolucionMedica;

  @Column(name = "fecha_alta")
  private Instant fechaAlta;

  @Column(name = "tipo_alta", length = 100)
  private String tipoAlta; // Mejoría, Traslado, Voluntaria, Fallecimiento

  @Column(name = "condicion_alta", length = 100)
  private String condicionAlta; // Buena, Regular, Grave

  @Column(name = "recomendaciones", columnDefinition = "TEXT")
  private String recomendaciones;

  @Column(name = "control_programado")
  private LocalDate controlProgramado;

  @Column(name = "especialidad_control", length = 100)
  private String especialidadControl;

  @Column(name = "fecha_creacion", updatable = false)
  private Instant fechaCreacion;
}