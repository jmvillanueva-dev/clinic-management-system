package com.clinic.webapi.modules.historiasclinicas.entity;

import com.clinic.webapi.modules.pacientes.model.entity.Paciente;
import com.clinic.webapi.shared.model.AuditableEntity;
import com.clinic.webapi.shared.util.EntityAuditListener;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "historias_clinicas")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EntityListeners(EntityAuditListener.class)
public class HistoriaClinica implements AuditableEntity {

  @Id
  @UuidGenerator
  private UUID id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "paciente_id", nullable = false, unique = true)
  private Paciente paciente;

  @Column(name = "numero_historia_clinica", nullable = false, unique = true, length = 20)
  private String numeroHistoriaClinica;

  @Column(name = "institucion_sistema", length = 200)
  private String institucionSistema = "Red Pública Integral de Salud";

  @Column(name = "unidad_operativa", length = 200)
  private String unidadOperativa = "Centro Médico Urdiales Espinoza";

  @Column(name = "cod_unidad", length = 20)
  private String codUnidad = "59890";

  @Column(name = "fecha_creacion", updatable = false)
  private Instant fechaCreacion;

  @Column(name = "fecha_actualizacion")
  private Instant fechaActualizacion;

  @OneToMany(mappedBy = "historiaClinica", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  // TODO: Implementar Modulo Evoluciones Medicas
  private List<com.clinic.webapi.modules.evolucionesmedicas.entity.EvolucionMedica> evolucionesMedicas = new ArrayList<>();
}
