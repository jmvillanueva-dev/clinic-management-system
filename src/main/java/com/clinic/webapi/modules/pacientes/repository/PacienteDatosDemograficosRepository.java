package com.clinic.webapi.modules.pacientes.repository;

import com.clinic.webapi.modules.pacientes.model.entity.Paciente;
import com.clinic.webapi.modules.pacientes.model.entity.PacienteDatosDemograficos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PacienteDatosDemograficosRepository extends JpaRepository<PacienteDatosDemograficos, UUID> {

  Optional<PacienteDatosDemograficos> findByPaciente(Paciente paciente);

  Optional<PacienteDatosDemograficos> findByPacienteId(UUID pacienteId);

  boolean existsByPaciente(Paciente paciente);
}