package com.clinic.webapi.repository;

import com.clinic.webapi.model.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PatientRepository extends JpaRepository<Patient, UUID> {
    Optional<Patient> findByIdentificationNumber(String identificationNumber);
}
