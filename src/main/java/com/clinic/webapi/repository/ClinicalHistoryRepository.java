package com.clinic.webapi.repository;

import com.clinic.webapi.model.entity.ClinicalHistory;
import com.clinic.webapi.model.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClinicalHistoryRepository extends JpaRepository<ClinicalHistory, UUID> {
    List<ClinicalHistory> findByPatient(Patient patient);
    Optional<ClinicalHistory> findByExternalNumber(String externalNumber);
}
