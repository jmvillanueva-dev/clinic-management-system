package com.clinic.webapi.modules.evolucionesmedicas.repository;

import com.clinic.webapi.modules.evolucionesmedicas.entity.EvolucionEmergenciaObstetrica;
import com.clinic.webapi.modules.evolucionesmedicas.entity.EvolucionMedica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EvolucionEmergenciaObstetricaRepository extends JpaRepository<EvolucionEmergenciaObstetrica, UUID> {

  Optional<EvolucionEmergenciaObstetrica> findByEvolucionMedica(EvolucionMedica evolucionMedica);

  Optional<EvolucionEmergenciaObstetrica> findByEvolucionMedicaId(UUID evolucionMedicaId);

  boolean existsByEvolucionMedicaId(UUID evolucionMedicaId);

  // Buscar emergencias obstétricas por semanas de gestación
  List<EvolucionEmergenciaObstetrica> findBySemanasGestacion(Integer semanasGestacion);

  // Buscar emergencias obstétricas con FPP en un rango
  List<EvolucionEmergenciaObstetrica> findByFppBetween(LocalDate startDate, LocalDate endDate);

  // Estadísticas de presentación fetal
  @Query("SELECT e.presentacion, COUNT(e) FROM EvolucionEmergenciaObstetrica e " +
      "WHERE e.presentacion IS NOT NULL " +
      "GROUP BY e.presentacion")
  List<Object[]> countByPresentacion();
}