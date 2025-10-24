package com.clinic.webapi.repository;

import com.clinic.webapi.model.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RolRepository extends JpaRepository<Rol, UUID> {

  Optional<Rol> findByNombre(String nombre);

  boolean existsByNombre(String nombre);
}