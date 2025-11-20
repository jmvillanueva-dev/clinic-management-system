package com.clinic.webapi.modules.evolucionesmedicas.repository;

import com.clinic.webapi.modules.evolucionesmedicas.entity.EvolucionMedica;
import com.clinic.webapi.modules.evolucionesmedicas.entity.EvolucionValoracionClinica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EvolucionValoracionClinicaRepository extends JpaRepository<EvolucionValoracionClinica, UUID> {

  Optional<EvolucionValoracionClinica> findByEvolucionMedica(EvolucionMedica evolucionMedica);

  Optional<EvolucionValoracionClinica> findByEvolucionMedicaId(UUID evolucionMedicaId);

  boolean existsByEvolucionMedicaId(UUID evolucionMedicaId);
}