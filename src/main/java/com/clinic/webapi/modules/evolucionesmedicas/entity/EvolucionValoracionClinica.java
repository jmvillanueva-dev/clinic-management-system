package com.clinic.webapi.modules.evolucionesmedicas.entity;

import com.clinic.webapi.shared.util.EntityAuditListener;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "evolucion_valoracion_clinica")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EntityListeners(EntityAuditListener.class)
public class EvolucionValoracionClinica {

  @Id
  @UuidGenerator
  private UUID id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "evolucion_medica_id", nullable = false, unique = true)
  private EvolucionMedica evolucionMedica;

  @Column(name = "inspeccion_general", columnDefinition = "TEXT")
  private String inspeccionGeneral;

  @Column(name = "cabeza_cuello", columnDefinition = "TEXT")
  private String cabezaCuello;

  @Column(name = "torax", columnDefinition = "TEXT")
  private String torax;

  @Column(name = "abdomen", columnDefinition = "TEXT")
  private String abdomen;

  @Column(name = "extremidades", columnDefinition = "TEXT")
  private String extremidades;

  @Column(name = "neurologico", columnDefinition = "TEXT")
  private String neurologico;

  @Column(name = "piel_tegumentos", columnDefinition = "TEXT")
  private String pielTegumentos;

  @Column(name = "otros_hallazgos", columnDefinition = "TEXT")
  private String otrosHallazgos;

  @Column(name = "fecha_creacion", updatable = false)
  private Instant fechaCreacion;
}