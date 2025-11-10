package com.clinic.webapi.modules.pacientes.repository;

import com.clinic.webapi.modules.pacientes.model.entity.Paciente;
import com.clinic.webapi.modules.pacientes.model.entity.PacienteContactoEmergencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PacienteContactoEmergenciaRepository extends JpaRepository<PacienteContactoEmergencia, UUID> {

  List<PacienteContactoEmergencia> findByPaciente(Paciente paciente);

  List<PacienteContactoEmergencia> findByPacienteId(UUID pacienteId);

  @Query("SELECT COUNT(c) FROM PacienteContactoEmergencia c WHERE c.paciente = :paciente")
  long countByPaciente(@Param("paciente") Paciente paciente);

  void deleteByPaciente(Paciente paciente);
}