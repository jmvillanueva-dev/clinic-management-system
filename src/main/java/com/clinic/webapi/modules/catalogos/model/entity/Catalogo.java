package com.clinic.webapi.modules.catalogos.model.entity;

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
@Table(name = "catalogos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EntityListeners(EntityAuditListener.class)
public class Catalogo implements AuditableEntity {

  @Id
  @UuidGenerator
  private UUID id;

  @Column(nullable = false, length = 100, unique = true)
  private String nombre;

  private String descripcion;

  @Column(nullable = false, length = 50)
  private String tipo; // 'SISTEMA', 'MEDICO'

  @Column(name = "esta_activo")
  private boolean estaActivo = true;

  @Column(name = "fecha_creacion", updatable = false)
  private Instant fechaCreacion;

  @Column(name = "fecha_actualizacion")
  private Instant fechaActualizacion;

  @OneToMany(mappedBy = "catalogo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<ItemCatalogo> items = new ArrayList<>();
}
