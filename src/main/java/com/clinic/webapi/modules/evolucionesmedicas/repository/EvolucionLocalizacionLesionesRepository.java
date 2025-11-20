package com.clinic.webapi.modules.evolucionesmedicas.repository;

import com.clinic.webapi.modules.evolucionesmedicas.entity.EvolucionLocalizacionLesiones;
import com.clinic.webapi.modules.evolucionesmedicas.entity.EvolucionMedica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EvolucionLocalizacionLesionesRepository extends JpaRepository<EvolucionLocalizacionLesiones, UUID> {

  List<EvolucionLocalizacionLesiones> findByEvolucionMedica(EvolucionMedica evolucionMedica);

  List<EvolucionLocalizacionLesiones> findByEvolucionMedicaId(UUID evolucionMedicaId);

  List<EvolucionLocalizacionLesiones> findByEvolucionMedicaIdAndGravedad(UUID evolucionMedicaId, String gravedad);

  // Buscar lesiones por tipo y gravedad
  @Query("SELECT l FROM EvolucionLocalizacionLesiones l WHERE " +
      "l.tipoLesion = :tipoLesion AND l.gravedad = :gravedad")
  List<EvolucionLocalizacionLesiones> findByTipoLesionAndGravedad(
      @Param("tipoLesion") String tipoLesion,
      @Param("gravedad") String gravedad);

  // Contar lesiones por evolución médica
  Long countByEvolucionMedicaId(UUID evolucionMedicaId);

  // Eliminar todas las lesiones de una evolución médica
  void deleteByEvolucionMedicaId(UUID evolucionMedicaId);
}