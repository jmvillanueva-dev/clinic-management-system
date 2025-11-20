package com.clinic.webapi.modules.evolucionesmedicas.repository;

import com.clinic.webapi.modules.evolucionesmedicas.entity.EvolucionIndicacionesMedicas;
import com.clinic.webapi.modules.evolucionesmedicas.entity.EvolucionPlanesTratamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EvolucionIndicacionesMedicasRepository extends JpaRepository<EvolucionIndicacionesMedicas, UUID> {

  List<EvolucionIndicacionesMedicas> findByPlanTratamiento(EvolucionPlanesTratamiento planTratamiento);

  List<EvolucionIndicacionesMedicas> findByPlanTratamientoId(UUID planTratamientoId);

  // Buscar indicaciones por medicamento
  List<EvolucionIndicacionesMedicas> findByMedicamentoContainingIgnoreCase(String medicamento);

  // Buscar indicaciones por vía de administración
  List<EvolucionIndicacionesMedicas> findByViaAdministracion(String viaAdministracion);

  // Medicamentos más prescritos
  @Query("SELECT i.medicamento, COUNT(i) FROM EvolucionIndicacionesMedicas i " +
      "WHERE i.medicamento IS NOT NULL " +
      "GROUP BY i.medicamento ORDER BY COUNT(i) DESC")
  List<Object[]> findMedicamentosMasPrescritos();

  // Eliminar todas las indicaciones de un plan de tratamiento
  void deleteByPlanTratamientoId(UUID planTratamientoId);
}