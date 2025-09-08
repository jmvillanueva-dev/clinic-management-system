package com.clinic.webapi.model.entity;

import lombok.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.*;

@Entity
@Table(name = "clinical_histories")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ClinicalHistory {

    @Id
    @UuidGenerator
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    private String externalNumber;
    private String chiefComplaint;
    @Column(columnDefinition = "TEXT")
    private String medicalHistory;
    private String allergies;
    private String socialHistory;
    private String familyHistory;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    private Instant deletedAt;
    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();

//    @OneToMany(mappedBy = "clinicalHistory", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Evolution> evolutions = new ArrayList<>();
}

