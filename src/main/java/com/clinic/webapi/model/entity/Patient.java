package com.clinic.webapi.model.entity;

import lombok.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

@Entity
@Table(name = "patients")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Patient {

    @Id
    @UuidGenerator
    private UUID id;

    private String identificationType;
    private String identificationNumber;

    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String gender;
    private String phone;
    private String email;
    private String address;

    private Instant deletedAt;
    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClinicalHistory> histories = new ArrayList<>();
}