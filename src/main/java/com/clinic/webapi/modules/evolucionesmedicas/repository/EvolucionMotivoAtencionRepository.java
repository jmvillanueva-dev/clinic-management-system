package com.clinic.webapi.modules.evolucionesmedicas.repository;

import com.clinic.webapi.modules.evolucionesmedicas.entity.EvolucionMedica;
import com.clinic.webapi.modules.evolucionesmedicas.entity.EvolucionMotivoAtencion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EvolucionMotivoAtencionRepository extends JpaRepository<EvolucionMotivoAtencion, UUID> {

  Optional<EvolucionMotivoAtencion> findByEvolucionMedica(EvolucionMedica evolucionMedica);

  Optional<EvolucionMotivoAtencion> findByEvolucionMedicaId(UUID evolucionMedicaId);

  boolean existsByEvolucionMedicaId(UUID evolucionMedicaId);
}