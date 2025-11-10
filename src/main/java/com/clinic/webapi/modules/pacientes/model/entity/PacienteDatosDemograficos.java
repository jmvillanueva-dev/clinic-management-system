package com.clinic.webapi.modules.pacientes.model.entity;

import com.clinic.webapi.modules.catalogos.model.entity.ItemCatalogo;
import com.clinic.webapi.shared.util.EntityAuditListener;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "pacientes_datos_demograficos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EntityListeners(EntityAuditListener.class)
public class PacienteDatosDemograficos {

  @Id
  @UuidGenerator
  private UUID id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "paciente_id", nullable = false, unique = true)
  private Paciente paciente;

  @Column(name = "fecha_nacimiento", nullable = false)
  private LocalDate fechaNacimiento;

  @Column(name = "lugar_nacimiento", length = 200)
  private String lugarNacimiento;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "genero_id", nullable = false)
  private ItemCatalogo genero;

  @Column(length = 100)
  private String nacionalidad;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "grupo_cultural_id")
  private ItemCatalogo grupoCultural;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "estado_civil_id")
  private ItemCatalogo estadoCivil;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "nivel_instruccion_id")
  private ItemCatalogo nivelInstruccion;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ubicacion_geografica_id")
  private ItemCatalogo ubicacionGeografica;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ocupacion_id")
  private ItemCatalogo ocupacion;
}