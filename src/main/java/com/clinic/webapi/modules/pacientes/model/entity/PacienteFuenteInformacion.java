package com.clinic.webapi.modules.pacientes.model.entity;

import com.clinic.webapi.modules.catalogos.model.entity.ItemCatalogo;
import com.clinic.webapi.shared.model.AuditableEntity;
import com.clinic.webapi.shared.util.EntityAuditListener;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "pacientes_fuente_informacion")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EntityListeners(EntityAuditListener.class)
public class PacienteFuenteInformacion implements AuditableEntity {

  @Id
  @UuidGenerator
  private UUID id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "paciente_id", nullable = false, unique = true)
  private Paciente paciente;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fuente_informacion_id", nullable = false)
  private ItemCatalogo fuenteInformacion;

  @Column(name = "nombre_fuente_info", nullable = false, length = 200)
  private String nombreFuenteInfo;

  @Column(nullable = false, length = 20)
  private String telefono;

  private String observaciones;

  @Column(name = "fecha_creacion", updatable = false)
  private Instant fechaCreacion;

  @Column(name = "fecha_actualizacion")
  private Instant fechaActualizacion;
}
