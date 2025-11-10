package com.clinic.webapi.modules.pacientes.model.entity;

import com.clinic.webapi.modules.catalogos.model.entity.ItemCatalogo;
import com.clinic.webapi.shared.util.EntityAuditListener;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "pacientes_antecedentes_clinicos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EntityListeners(EntityAuditListener.class)
public class PacienteAntecedenteClinico {

  @Id
  @UuidGenerator
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "paciente_id", nullable = false)
  private Paciente paciente;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "tipo_antecedente_id", nullable = false)
  private ItemCatalogo tipoAntecedente;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "patologia_id", nullable = false)
  private ItemCatalogo patologia;

  private String descripcion;

  @Column(name = "fecha_diagnostico")
  private LocalDate fechaDiagnostico;

  private String tratamiento;

  @Column(name = "esta_activo")
  private boolean estaActivo = true;

  @Column(name = "fecha_creacion", updatable = false)
  private Instant fechaCreacion;
}