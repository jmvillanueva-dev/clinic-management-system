package com.clinic.webapi.modules.pacientes.repository;

import com.clinic.webapi.modules.pacientes.model.entity.Paciente;
import com.clinic.webapi.modules.pacientes.model.entity.PacienteAntecedenteClinico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PacienteAntecedenteClinicoRepository extends JpaRepository<PacienteAntecedenteClinico, UUID> {

  List<PacienteAntecedenteClinico> findByPaciente(Paciente paciente);

  List<PacienteAntecedenteClinico> findByPacienteId(UUID pacienteId);

  List<PacienteAntecedenteClinico> findByPacienteAndEstaActivo(Paciente paciente, boolean estaActivo);

  @Query("SELECT a FROM PacienteAntecedenteClinico a WHERE a.paciente = :paciente AND a.estaActivo = true")
  List<PacienteAntecedenteClinico> findActiveByPaciente(@Param("paciente") Paciente paciente);

  @Query("SELECT COUNT(a) FROM PacienteAntecedenteClinico a WHERE a.paciente = :paciente AND a.estaActivo = true")
  long countActiveByPaciente(@Param("paciente") Paciente paciente);

  void deleteByPaciente(Paciente paciente);
}