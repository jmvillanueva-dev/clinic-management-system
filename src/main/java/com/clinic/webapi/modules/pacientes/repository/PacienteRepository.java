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

  @Query("SELECT p FROM Paciente p " +
      "LEFT JOIN FETCH p.grupoSanguineo " +
      "LEFT JOIN FETCH p.datosDemograficos dd " +
      "LEFT JOIN FETCH dd.genero " +
      "LEFT JOIN FETCH dd.grupoCultural " +
      "LEFT JOIN FETCH dd.estadoCivil " +
      "LEFT JOIN FETCH dd.nivelInstruccion " +
      "LEFT JOIN FETCH p.ubicacionGeografica ug " +
      "LEFT JOIN FETCH ug.provincia " +
      "LEFT JOIN FETCH p.ocupacion o " +
      "LEFT JOIN FETCH o.ocupacion " +
      "LEFT JOIN FETCH p.fuenteInformacion fi " +
      "LEFT JOIN FETCH fi.fuenteInformacion " +
      "LEFT JOIN FETCH p.contactosEmergencia ce " +
      "LEFT JOIN FETCH ce.parentesco " +
      "LEFT JOIN FETCH p.antecedentesClinicos ac " +
      "LEFT JOIN FETCH ac.tipoAntecedente " +
      "LEFT JOIN FETCH ac.patologia " +
      "WHERE p.id = :id")
  Optional<Paciente> findByIdWithDetails(@Param("id") UUID id);

  @Query("SELECT p FROM Paciente p WHERE p.estaActivo = true " +
      "AND (LOWER(p.primerNombre) LIKE LOWER(CONCAT('%', :search, '%')) " +
      "OR LOWER(p.apellidoPaterno) LIKE LOWER(CONCAT('%', :search, '%')) " +
      "OR p.cedula LIKE CONCAT('%', :search, '%'))")
  List<Paciente> searchActivePacientes(@Param("search") String search);
}