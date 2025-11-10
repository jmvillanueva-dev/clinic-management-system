package com.clinic.webapi.modules.catalogos.model.entity;

import com.clinic.webapi.shared.util.EntityAuditListener;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "items_catalogo")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EntityListeners(EntityAuditListener.class)
public class ItemCatalogo {

  @Id
  @UuidGenerator
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "catalogo_id", nullable = false)
  private Catalogo catalogo;

  @Column(nullable = false, length = 200)
  private String nombre;

  private String descripcion;

  @Column(length = 50)
  private String codigo;

  private String valor;

  @Column(name = "esta_activo")
  private boolean estaActivo = true;

  @Column(name = "fecha_creacion", updatable = false)
  private Instant fechaCreacion;

  @Column(name = "fecha_actualizacion")
  private Instant fechaActualizacion;
}