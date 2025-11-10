package com.clinic.webapi.modules.pacientes.repository;

import com.clinic.webapi.modules.pacientes.model.entity.Paciente;
import com.clinic.webapi.modules.pacientes.model.entity.PacienteFuenteInformacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PacienteFuenteInformacionRepository extends JpaRepository<PacienteFuenteInformacion, UUID> {

  Optional<PacienteFuenteInformacion> findByPaciente(Paciente paciente);

  Optional<PacienteFuenteInformacion> findByPacienteId(UUID pacienteId);

  boolean existsByPaciente(Paciente paciente);
}