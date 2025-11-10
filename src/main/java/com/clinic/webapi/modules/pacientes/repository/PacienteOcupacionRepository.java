package com.clinic.webapi.modules.pacientes.repository;

import com.clinic.webapi.modules.pacientes.model.entity.Paciente;
import com.clinic.webapi.modules.pacientes.model.entity.PacienteOcupacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PacienteOcupacionRepository extends JpaRepository<PacienteOcupacion, UUID> {

  Optional<PacienteOcupacion> findByPaciente(Paciente paciente);

  Optional<PacienteOcupacion> findByPacienteId(UUID pacienteId);

  boolean existsByPaciente(Paciente paciente);
}