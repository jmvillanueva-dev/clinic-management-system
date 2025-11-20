package com.clinic.webapi.modules.evolucionesmedicas.entity;

import com.clinic.webapi.shared.model.AuditableEntity;
import com.clinic.webapi.shared.util.EntityAuditListener;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "evolucion_emergencia_obstetrica")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EntityListeners(EntityAuditListener.class)
public class EvolucionEmergenciaObstetrica implements AuditableEntity {

  @Id
  @UuidGenerator
  private UUID id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "evolucion_medica_id", nullable = false, unique = true)
  private EvolucionMedica evolucionMedica;

  @Column(name = "gestas_previas")
  private Integer gestasPrevias;

  @Column(name = "partos_previos")
  private Integer partosPrevios;

  @Column(name = "abortos_previos")
  private Integer abortosPrevios;

  @Column(name = "fum")
  private LocalDate fum; // Fecha última menstruación

  @Column(name = "fpp")
  private LocalDate fpp; // Fecha probable parto

  @Column(name = "semanas_gestacion")
  private Integer semanasGestacion;

  @Column(name = "presentacion", length = 100)
  private String presentacion;

  @Column(name = "dinamica_uterina", columnDefinition = "TEXT")
  private String dinamicaUterina;

  @Column(name = "latidos_fetales", length = 100)
  private String latidosFetales;

  @Column(name = "observaciones", columnDefinition = "TEXT")
  private String observaciones;

  @Column(name = "fecha_creacion", updatable = false)
  private Instant fechaCreacion;

  @Column(name = "fecha_actualizacion")
  private Instant fechaActualizacion;
}
