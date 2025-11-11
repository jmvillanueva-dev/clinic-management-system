package com.clinic.webapi.modules.evolucionesmedicas.entity;

import com.clinic.webapi.modules.empleados.entity.Empleado;
import com.clinic.webapi.modules.historiasclinicas.entity.HistoriaClinica;
import com.clinic.webapi.shared.util.EntityAuditListener;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "evoluciones_medicas")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EntityListeners(EntityAuditListener.class)
public class EvolucionMedica {

  @Id
  @UuidGenerator
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "historia_clinica_id", nullable = false)
  private HistoriaClinica historiaClinica;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "empleado_id", nullable = false)
  private Empleado empleado;

  @Column(name = "fecha_consulta")
  private Instant fechaConsulta;

  @Column(name = "tipo_consulta", length = 100)
  private String tipoConsulta; // Primera vez, control, emergencia, etc.

  @Column(name = "estado", length = 50)
  @Builder.Default
  private String estado = "ACTIVA"; // ACTIVA, CERRADA, CANCELADA

  @Column(name = "observaciones_generales", columnDefinition = "TEXT")
  private String observacionesGenerales;

  @Column(name = "fecha_creacion", updatable = false)
  private Instant fechaCreacion;

  @Column(name = "fecha_actualizacion")
  private Instant fechaActualizacion;

  // Relaciones con las secciones
  @OneToOne(mappedBy = "evolucionMedica", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private EvolucionMotivoAtencion motivoAtencion;

  @OneToOne(mappedBy = "evolucionMedica", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private EvolucionAntecedentesIncidente antecedentesIncidente;

  @OneToOne(mappedBy = "evolucionMedica", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private EvolucionSignosVitales signosVitales;

  @OneToOne(mappedBy = "evolucionMedica", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private EvolucionValoracionClinica valoracionClinica;

  @OneToOne(mappedBy = "evolucionMedica", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private EvolucionEmergenciaObstetrica emergenciaObstetrica;

  @OneToOne(mappedBy = "evolucionMedica", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private EvolucionAltaMedica altaMedica;

  @OneToMany(mappedBy = "evolucionMedica", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @Builder.Default
  private List<EvolucionLocalizacionLesiones> localizacionLesiones = new ArrayList<>();

  @OneToMany(mappedBy = "evolucionMedica", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @Builder.Default
  private List<EvolucionExamenesSolicitados> examenesSolicitados = new ArrayList<>();

  @OneToMany(mappedBy = "evolucionMedica", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @Builder.Default
  private List<EvolucionDiagnosticos> diagnosticos = new ArrayList<>();

  @OneToMany(mappedBy = "evolucionMedica", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @Builder.Default
  private List<EvolucionPlanesTratamiento> planesTratamiento = new ArrayList<>();
}