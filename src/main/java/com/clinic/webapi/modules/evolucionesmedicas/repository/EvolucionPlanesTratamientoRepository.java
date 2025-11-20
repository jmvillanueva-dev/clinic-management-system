package com.clinic.webapi.modules.evolucionesmedicas.repository;

import com.clinic.webapi.modules.evolucionesmedicas.entity.EvolucionMedica;
import com.clinic.webapi.modules.evolucionesmedicas.entity.EvolucionPlanesTratamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EvolucionPlanesTratamientoRepository extends JpaRepository<EvolucionPlanesTratamiento, UUID> {

  List<EvolucionPlanesTratamiento> findByEvolucionMedica(EvolucionMedica evolucionMedica);

  List<EvolucionPlanesTratamiento> findByEvolucionMedicaId(UUID evolucionMedicaId);

  List<EvolucionPlanesTratamiento> findByEvolucionMedicaIdAndTipoTratamiento(UUID evolucionMedicaId, String tipoTratamiento);

  // Buscar planes de tratamiento por tipo
  List<EvolucionPlanesTratamiento> findByTipoTratamiento(String tipoTratamiento);

  // Contar planes de tratamiento por tipo
  @Query("SELECT p.tipoTratamiento, COUNT(p) FROM EvolucionPlanesTratamiento p " +
      "GROUP BY p.tipoTratamiento")
  List<Object[]> countByTipoTratamiento();

  // Eliminar todos los planes de tratamiento de una evolución médica
  void deleteByEvolucionMedicaId(UUID evolucionMedicaId);
}