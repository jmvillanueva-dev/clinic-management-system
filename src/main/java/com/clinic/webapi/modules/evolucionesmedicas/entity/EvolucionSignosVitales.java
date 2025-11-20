package com.clinic.webapi.modules.evolucionesmedicas.entity;

import com.clinic.webapi.shared.model.AuditableEntity;
import com.clinic.webapi.shared.util.EntityAuditListener;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "evolucion_signos_vitales")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EntityListeners(EntityAuditListener.class)
public class EvolucionSignosVitales implements AuditableEntity {

  @Id
  @UuidGenerator
  private UUID id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "evolucion_medica_id", nullable = false, unique = true)
  private EvolucionMedica evolucionMedica;

  @Column(name = "presion_arterial_sistolica")
  private Integer presionArterialSistolica;

  @Column(name = "presion_arterial_diastolica")
  private Integer presionArterialDiastolica;

  @Column(name = "frecuencia_cardiaca")
  private Integer frecuenciaCardiaca;

  @Column(name = "frecuencia_respiratoria")
  private Integer frecuenciaRespiratoria;

  @Column(name = "temperatura", precision = 4, scale = 2)
  private BigDecimal temperatura;

  @Column(name = "saturacion_oxigeno")
  private Integer saturacionOxigeno;

  @Column(name = "peso", precision = 5, scale = 2)
  private BigDecimal peso;

  @Column(name = "talla", precision = 5, scale = 2)
  private BigDecimal talla;

  @Column(name = "imc", precision = 4, scale = 2)
  private BigDecimal imc;

  @Column(name = "glucosa", precision = 5, scale = 2)
  private BigDecimal glucosa;

  @Column(name = "fecha_creacion", updatable = false)
  private Instant fechaCreacion;

  @Column(name = "fecha_actualizacion")
  private Instant fechaActualizacion;
}
