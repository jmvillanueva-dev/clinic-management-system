package com.clinic.webapi.modules.pacientes.model.entity;

import com.clinic.webapi.modules.catalogos.model.entity.ItemCatalogo;
import com.clinic.webapi.shared.util.EntityAuditListener;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "pacientes")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EntityListeners(EntityAuditListener.class)
public class Paciente {

  @Id
  @UuidGenerator
  private UUID id;

  @Column(nullable = false, length = 20, unique = true)
  private String cedula;

  @Column(name = "primer_nombre", nullable = false, length = 100)
  private String primerNombre;

  @Column(name = "segundo_nombre", length = 100)
  private String segundoNombre;

  @Column(name = "apellido_paterno", nullable = false, length = 100)
  private String apellidoPaterno;

  @Column(name = "apellido_materno", length = 100)
  private String apellidoMaterno;

  @Column(length = 255)
  private String email;

  @Column(length = 20)
  private String telefono;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "grupo_sanguineo_id")
  private ItemCatalogo grupoSanguineo;

  @Column(name = "fecha_creacion", updatable = false)
  private Instant fechaCreacion;

  @Column(name = "fecha_actualizacion")
  private Instant fechaActualizacion;

  @Column(name = "esta_activo")
  private boolean estaActivo = true;

  // Relaciones
  @OneToOne(mappedBy = "paciente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private PacienteOcupacion ocupacion;

  @OneToOne(mappedBy = "paciente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private PacienteDatosDemograficos datosDemograficos;

  @OneToOne(mappedBy = "paciente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private PacienteUbicacionGeografica ubicacionGeografica;

  @OneToOne(mappedBy = "paciente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private PacienteFuenteInformacion fuenteInformacion;

  @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<PacienteContactoEmergencia> contactosEmergencia = new ArrayList<>();

  @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<PacienteAntecedenteClinico> antecedentesClinicos = new ArrayList<>();
}