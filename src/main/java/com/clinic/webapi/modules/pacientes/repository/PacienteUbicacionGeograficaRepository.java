package com.clinic.webapi.modules.pacientes.repository;

import com.clinic.webapi.modules.pacientes.model.entity.Paciente;
import com.clinic.webapi.modules.pacientes.model.entity.PacienteUbicacionGeografica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PacienteUbicacionGeograficaRepository extends JpaRepository<PacienteUbicacionGeografica, UUID> {

  Optional<PacienteUbicacionGeografica> findByPaciente(Paciente paciente);

  Optional<PacienteUbicacionGeografica> findByPacienteId(UUID pacienteId);

  boolean existsByPaciente(Paciente paciente);
}