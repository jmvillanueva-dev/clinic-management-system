package com.clinic.webapi.modules.evolucionesmedicas.repository;

import com.clinic.webapi.modules.evolucionesmedicas.entity.EvolucionMedica;
import com.clinic.webapi.modules.evolucionesmedicas.entity.EvolucionSignosVitales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EvolucionSignosVitalesRepository extends JpaRepository<EvolucionSignosVitales, UUID> {

  Optional<EvolucionSignosVitales> findByEvolucionMedica(EvolucionMedica evolucionMedica);

  Optional<EvolucionSignosVitales> findByEvolucionMedicaId(UUID evolucionMedicaId);

  boolean existsByEvolucionMedicaId(UUID evolucionMedicaId);

  // Buscar signos vitales con valores anormales
  @Query("SELECT s FROM EvolucionSignosVitales s WHERE " +
      "s.presionArterialSistolica > 140 OR s.presionArterialDiastolica > 90 OR " +
      "s.frecuenciaCardiaca > 100 OR s.frecuenciaCardiaca < 60 OR " +
      "s.temperatura > 38.0 OR s.saturacionOxigeno < 95")
  List<EvolucionSignosVitales> findSignosVitalesAnormales();

  // Promedio de signos vitales por paciente
  @Query("SELECT AVG(s.presionArterialSistolica), AVG(s.presionArterialDiastolica), " +
      "AVG(s.frecuenciaCardiaca), AVG(s.temperatura) " +
      "FROM EvolucionSignosVitales s " +
      "WHERE s.evolucionMedica.historiaClinica.id = :historiaClinicaId")
  Object[] findPromedioSignosVitalesByHistoriaClinica(@Param("historiaClinicaId") UUID historiaClinicaId);
}