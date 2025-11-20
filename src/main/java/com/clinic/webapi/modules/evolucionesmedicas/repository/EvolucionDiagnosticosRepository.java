package com.clinic.webapi.modules.evolucionesmedicas.repository;

import com.clinic.webapi.modules.evolucionesmedicas.entity.EvolucionDiagnosticos;
import com.clinic.webapi.modules.evolucionesmedicas.entity.EvolucionMedica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EvolucionDiagnosticosRepository extends JpaRepository<EvolucionDiagnosticos, UUID> {

  List<EvolucionDiagnosticos> findByEvolucionMedica(EvolucionMedica evolucionMedica);

  List<EvolucionDiagnosticos> findByEvolucionMedicaId(UUID evolucionMedicaId);

  List<EvolucionDiagnosticos> findByEvolucionMedicaIdAndTipo(UUID evolucionMedicaId, String tipo);

  // Buscar diagnósticos por código CIE
  List<EvolucionDiagnosticos> findByCodigoCie(String codigoCie);

  // Buscar diagnósticos que contengan cierto texto
  @Query("SELECT d FROM EvolucionDiagnosticos d WHERE " +
      "LOWER(d.diagnostico) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
      "LOWER(d.codigoCie) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
  List<EvolucionDiagnosticos> findByDiagnosticoContaining(@Param("searchTerm") String searchTerm);

  // Diagnósticos más comunes
  @Query("SELECT d.diagnostico, COUNT(d) FROM EvolucionDiagnosticos d " +
      "GROUP BY d.diagnostico ORDER BY COUNT(d) DESC")
  List<Object[]> findDiagnosticosMasComunes();

  // Eliminar todos los diagnósticos de una evolución médica
  void deleteByEvolucionMedicaId(UUID evolucionMedicaId);
}