package com.clinic.webapi.repository;

import com.clinic.webapi.model.entity.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, UUID> {

  Optional<Empleado> findByCedula(String cedula);

  boolean existsByCedula(String cedula);

  List<Empleado> findAllByEstaActivo(boolean estaActivo);
}