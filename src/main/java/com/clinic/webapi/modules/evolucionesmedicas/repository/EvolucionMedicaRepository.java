package com.clinic.webapi.modules.evolucionesmedicas.repository;

import com.clinic.webapi.modules.evolucionesmedicas.entity.EvolucionMedica;
import com.clinic.webapi.modules.historiasclinicas.entity.HistoriaClinica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EvolucionMedicaRepository extends JpaRepository<EvolucionMedica, UUID> {

  // Buscar evoluciones por historia clínica
  List<EvolucionMedica> findByHistoriaClinicaIdOrderByFechaConsultaDesc(UUID historiaClinicaId);

  List<EvolucionMedica> findByHistoriaClinicaOrderByFechaConsultaDesc(HistoriaClinica historiaClinica);

  // Buscar evoluciones por empleado (médico)
  List<EvolucionMedica> findByEmpleadoIdOrderByFechaConsultaDesc(UUID empleadoId);

  // Buscar evoluciones por estado
  List<EvolucionMedica> findByEstadoOrderByFechaConsultaDesc(String estado);

  // Buscar evoluciones por rango de fechas
  List<EvolucionMedica> findByFechaConsultaBetweenOrderByFechaConsultaDesc(Instant startDate, Instant endDate);

  // Contar evoluciones por historia clínica
  Long countByHistoriaClinicaId(UUID historiaClinicaId);

  // Verificar existencia
  boolean existsByHistoriaClinicaId(UUID historiaClinicaId);

  // Buscar última evolución por historia clínica
  Optional<EvolucionMedica> findFirstByHistoriaClinicaIdOrderByFechaConsultaDesc(UUID historiaClinicaId);

  // Buscar evoluciones con filtros combinados
  @Query("SELECT e FROM EvolucionMedica e WHERE " +
      "(:historiaClinicaId IS NULL OR e.historiaClinica.id = :historiaClinicaId) AND " +
      "(:empleadoId IS NULL OR e.empleado.id = :empleadoId) AND " +
      "(:estado IS NULL OR e.estado = :estado) AND " +
      "(:fechaInicio IS NULL OR e.fechaConsulta >= :fechaInicio) AND " +
      "(:fechaFin IS NULL OR e.fechaConsulta <= :fechaFin) " +
      "ORDER BY e.fechaConsulta DESC")
  List<EvolucionMedica> findByFiltros(
      @Param("historiaClinicaId") UUID historiaClinicaId,
      @Param("empleadoId") UUID empleadoId,
      @Param("estado") String estado,
      @Param("fechaInicio") Instant fechaInicio,
      @Param("fechaFin") Instant fechaFin);

  // Obtener evolución con todas las relaciones (para detalles completos)
  @Query("SELECT e FROM EvolucionMedica e " +
      "LEFT JOIN FETCH e.motivoAtencion " +
      "LEFT JOIN FETCH e.antecedentesIncidente " +
      "LEFT JOIN FETCH e.signosVitales " +
      "LEFT JOIN FETCH e.valoracionClinica " +
      "LEFT JOIN FETCH e.emergenciaObstetrica " +
      "LEFT JOIN FETCH e.altaMedica " +
      "WHERE e.id = :id")
  Optional<EvolucionMedica> findByIdWithAllSections(@Param("id") UUID id);

  // Obtener evolución básica (para resúmenes)
  @Query("SELECT e FROM EvolucionMedica e " +
      "LEFT JOIN FETCH e.historiaClinica hc " +
      "LEFT JOIN FETCH e.empleado emp " +
      "WHERE e.id = :id")
  Optional<EvolucionMedica> findByIdWithBasicInfo(@Param("id") UUID id);

  // Estadísticas por médico
  @Query("SELECT e.empleado.id, COUNT(e) FROM EvolucionMedica e " +
      "WHERE e.fechaConsulta BETWEEN :startDate AND :endDate " +
      "GROUP BY e.empleado.id")
  List<Object[]> countEvolucionesByEmpleadoInPeriod(@Param("startDate") Instant startDate,
                                                    @Param("endDate") Instant endDate);
}