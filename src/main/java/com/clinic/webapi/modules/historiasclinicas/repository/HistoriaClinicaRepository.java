package com.clinic.webapi.modules.historiasclinicas.repository;

import com.clinic.webapi.modules.historiasclinicas.entity.HistoriaClinica;
import com.clinic.webapi.modules.pacientes.model.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface HistoriaClinicaRepository extends JpaRepository<HistoriaClinica, UUID> {

  Optional<HistoriaClinica> findByPacienteId(UUID pacienteId);

  Optional<HistoriaClinica> findByPaciente(Paciente paciente);

  Optional<HistoriaClinica> findByNumeroHistoriaClinica(String numeroHistoriaClinica);

  boolean existsByPacienteId(UUID pacienteId);

  boolean existsByPaciente(Paciente paciente);

  boolean existsByNumeroHistoriaClinica(String numeroHistoriaClinica);

  @Query("SELECT COUNT(e) FROM EvolucionMedica e WHERE e.historiaClinica.id = :historiaClinicaId")
  Long countEvolucionesByHistoriaClinicaId(@Param("historiaClinicaId") UUID historiaClinicaId);
}