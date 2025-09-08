package com.clinic.webapi.model.entity;

import lombok.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.Set;
import java.util.HashSet;
import java.util.UUID;


@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {

    @Id
    @UuidGenerator
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(unique = true)
    private String username;

    @Column(nullable = false)
    private String passwordHash;

    private String firstName;
    private String lastName;
    private String phone;

    private boolean isActive = true;
    private boolean isVerified = false;

    private Instant deletedAt;
    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id")
    )
    private Set<Role> roles = new HashSet<>();
}
