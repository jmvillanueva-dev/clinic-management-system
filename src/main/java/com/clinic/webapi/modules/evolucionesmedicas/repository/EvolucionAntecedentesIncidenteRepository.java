package com.clinic.webapi.modules.evolucionesmedicas.repository;

import com.clinic.webapi.modules.evolucionesmedicas.entity.EvolucionAntecedentesIncidente;
import com.clinic.webapi.modules.evolucionesmedicas.entity.EvolucionMedica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EvolucionAntecedentesIncidenteRepository extends JpaRepository<EvolucionAntecedentesIncidente, UUID> {

  Optional<EvolucionAntecedentesIncidente> findByEvolucionMedica(EvolucionMedica evolucionMedica);

  Optional<EvolucionAntecedentesIncidente> findByEvolucionMedicaId(UUID evolucionMedicaId);

  boolean existsByEvolucionMedicaId(UUID evolucionMedicaId);
}