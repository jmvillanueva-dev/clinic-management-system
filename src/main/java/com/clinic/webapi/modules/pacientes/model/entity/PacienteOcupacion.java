package com.clinic.webapi.modules.pacientes.model.entity;

import com.clinic.webapi.modules.catalogos.model.entity.ItemCatalogo;
import com.clinic.webapi.shared.model.AuditableEntity;
import com.clinic.webapi.shared.util.EntityAuditListener;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "pacientes_ocupacion")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EntityListeners(EntityAuditListener.class)
public class PacienteOcupacion implements AuditableEntity {

  @Id
  @UuidGenerator
  private UUID id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "paciente_id", nullable = false, unique = true)
  private Paciente paciente;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ocupacion_id", nullable = false)
  private ItemCatalogo ocupacion;

  @Column(name = "nombre_empresa", length = 200)
  private String nombreEmpresa;

  @Column(length = 100)
  private String cargo;

  @Column(name = "telefono_empresa", length = 20)
  private String telefonoEmpresa;

  @Column(name = "direccion_empresa")
  private String direccionEmpresa;

  @Column(name = "fecha_inicio")
  private LocalDate fechaInicio;

  @Column(name = "fecha_fin")
  private LocalDate fechaFin;

  private boolean actual = true;

  @Column(name = "fecha_creacion", updatable = false)
  private Instant fechaCreacion;

  @Column(name = "fecha_actualizacion")
  private Instant fechaActualizacion;
}
