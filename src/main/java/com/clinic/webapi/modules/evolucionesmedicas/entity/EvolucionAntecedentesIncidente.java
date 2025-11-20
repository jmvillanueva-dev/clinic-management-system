package com.clinic.webapi.modules.evolucionesmedicas.entity;

import com.clinic.webapi.shared.model.AuditableEntity;
import com.clinic.webapi.shared.util.EntityAuditListener;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "evolucion_antecedentes_incidente")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EntityListeners(EntityAuditListener.class)
public class EvolucionAntecedentesIncidente implements AuditableEntity {

  @Id
  @UuidGenerator
  private UUID id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "evolucion_medica_id", nullable = false, unique = true)
  private EvolucionMedica evolucionMedica;

  @Column(name = "antecedentes_personales", columnDefinition = "TEXT")
  private String antecedentesPersonales;

  @Column(name = "antecedentes_familiares", columnDefinition = "TEXT")
  private String antecedentesFamiliares;

  @Column(name = "habitos_toxicos", columnDefinition = "TEXT")
  private String habitosToxicos;

  @Column(name = "alergias", columnDefinition = "TEXT")
  private String alergias;

  @Column(name = "medicamentos_actuales", columnDefinition = "TEXT")
  private String medicamentosActuales;

  @Column(name = "fecha_creacion", updatable = false)
  private Instant fechaCreacion;

  @Column(name = "fecha_actualizacion")
  private Instant fechaActualizacion;
}
