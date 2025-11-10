package com.clinic.webapi.modules.pacientes.repository;

import com.clinic.webapi.modules.pacientes.model.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, UUID> {

  Optional<Paciente> findByCedula(String cedula);

  boolean existsByCedula(String cedula);

  List<Paciente> findAllByEstaActivo(boolean estaActivo);

  @Query("SELECT p FROM Paciente p LEFT JOIN FETCH p.datosDemograficos " +
      "LEFT JOIN FETCH p.ubicacionGeografica LEFT JOIN FETCH p.ocupacion " +
      "LEFT JOIN FETCH p.fuenteInformacion WHERE p.id = :id")
  Optional<Paciente> findByIdWithDetails(@Param("id") UUID id);

  @Query("SELECT p FROM Paciente p WHERE p.estaActivo = true " +
      "AND (LOWER(p.primerNombre) LIKE LOWER(CONCAT('%', :search, '%')) " +
      "OR LOWER(p.apellidoPaterno) LIKE LOWER(CONCAT('%', :search, '%')) " +
      "OR p.cedula LIKE CONCAT('%', :search, '%'))")
  List<Paciente> searchActivePacientes(@Param("search") String search);
}