package com.clinic.webapi.modules.evolucionesmedicas.repository;

import com.clinic.webapi.modules.evolucionesmedicas.entity.EvolucionExamenesSolicitados;
import com.clinic.webapi.modules.evolucionesmedicas.entity.EvolucionMedica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EvolucionExamenesSolicitadosRepository extends JpaRepository<EvolucionExamenesSolicitados, UUID> {

  List<EvolucionExamenesSolicitados> findByEvolucionMedica(EvolucionMedica evolucionMedica);

  List<EvolucionExamenesSolicitados> findByEvolucionMedicaId(UUID evolucionMedicaId);

  List<EvolucionExamenesSolicitados> findByEvolucionMedicaIdAndEstado(UUID evolucionMedicaId, String estado);

  List<EvolucionExamenesSolicitados> findByEstado(String estado);

  // Buscar exámenes por tipo y urgencia
  @Query("SELECT e FROM EvolucionExamenesSolicitados e WHERE " +
      "e.tipoExamen = :tipoExamen AND e.urgencia = :urgencia")
  List<EvolucionExamenesSolicitados> findByTipoExamenAndUrgencia(
      @Param("tipoExamen") String tipoExamen,
      @Param("urgencia") String urgencia);

  // Contar exámenes por estado
  Long countByEstado(String estado);

  // Eliminar todos los exámenes de una evolución médica
  void deleteByEvolucionMedicaId(UUID evolucionMedicaId);
}