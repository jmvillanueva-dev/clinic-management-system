package com.clinic.webapi.modules.evolucionesmedicas.repository;

import com.clinic.webapi.modules.evolucionesmedicas.entity.EvolucionAltaMedica;
import com.clinic.webapi.modules.evolucionesmedicas.entity.EvolucionMedica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EvolucionAltaMedicaRepository extends JpaRepository<EvolucionAltaMedica, UUID> {

  Optional<EvolucionAltaMedica> findByEvolucionMedica(EvolucionMedica evolucionMedica);

  Optional<EvolucionAltaMedica> findByEvolucionMedicaId(UUID evolucionMedicaId);

  boolean existsByEvolucionMedicaId(UUID evolucionMedicaId);

  // Buscar altas médicas por tipo
  List<EvolucionAltaMedica> findByTipoAlta(String tipoAlta);

  // Buscar altas médicas por condición al alta
  List<EvolucionAltaMedica> findByCondicionAlta(String condicionAlta);

  // Buscar altas médicas en un rango de fechas
  List<EvolucionAltaMedica> findByFechaAltaBetween(Instant startDate, Instant endDate);

  // Estadísticas de tipos de alta
  @Query("SELECT a.tipoAlta, COUNT(a) FROM EvolucionAltaMedica a " +
      "GROUP BY a.tipoAlta")
  List<Object[]> countByTipoAlta();

  // Promedio de días hasta el control programado
  @Query(value = "SELECT AVG(EXTRACT(DAY FROM (a.control_programado - a.fecha_alta))) " +
      "FROM evolucion_alta_medica a WHERE a.control_programado IS NOT NULL",
      nativeQuery = true)
  Double findPromedioDiasHastaControl();
}