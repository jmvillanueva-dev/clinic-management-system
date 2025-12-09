package com.clinic.webapi.modules.auth.repository;

import com.clinic.webapi.modules.empleados.entity.Empleado;
import com.clinic.webapi.modules.auth.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;


@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

  Optional<Usuario> findByEmail(String email);

  // Buscar todos los usuarios asociados a un empleado
  List<Usuario> findAllByEmpleado(Empleado empleado);

  Optional<Usuario> findByEmpleado(Empleado empleado);

  Optional<Usuario> findByTokenVerificacion(String token);

  Optional<Usuario> findByTokenRecuperacion(String tokenRecuperacion);
}
