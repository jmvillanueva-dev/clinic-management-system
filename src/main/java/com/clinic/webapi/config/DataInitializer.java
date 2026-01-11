package com.clinic.webapi.config;

import com.clinic.webapi.modules.empleados.entity.Rol;
import com.clinic.webapi.modules.empleados.entity.Empleado;
import com.clinic.webapi.modules.auth.entity.Usuario;
import com.clinic.webapi.modules.empleados.repository.RolRepository;
import com.clinic.webapi.modules.empleados.repository.EmpleadoRepository;
import com.clinic.webapi.modules.auth.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

  private final RolRepository rolRepository;
  private final EmpleadoRepository empleadoRepository;
  private final UsuarioRepository usuarioRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public void run(String... args) throws Exception {
    // --- 1. Crear Roles ---
    Rol adminRol = rolRepository.findByNombre("ADMINISTRADOR").orElseGet(() ->
        rolRepository.save(Rol.builder()
            .nombre("ADMINISTRADOR")
            .area("Administración")
            .descripcion("Máximos privilegios del sistema. Brinda acceso a todas las funcionalidades y configuraciones.")
            .build()));

    rolRepository.findByNombre("MEDICO").orElseGet(() ->
        rolRepository.save(Rol.builder()
            .nombre("MEDICO")
            .area("Atención Médica")
            .descripcion("Permisos para gestión de pacientes, historias clínicas y evoluciones médicas.")
            .build()));

    // --- 2. Crear Empleado (ADMIN) ---
    if (empleadoRepository.findByCedula("0000000000").isEmpty()) {
      Empleado adminEmpleado = Empleado.builder()
          .nombre("Administrador")
          .apellido("Técnico")
          .cedula("0000000000")
          .telefono("9999999999")
          .estaActivo(true)
          .build();
      adminEmpleado = empleadoRepository.save(adminEmpleado);

      // --- 3. Crear Usuario (ADMIN) y CODIFICAR contraseña ---
      Usuario adminUsuario = Usuario.builder()
          .email("jhonny.villanueva@epn.edu.ec")
          .passwordHash(passwordEncoder.encode("admin_clinic_ue_2026"))
          .empleado(adminEmpleado)
          .roles(new HashSet<>(Collections.singletonList(adminRol)))
          .estaActivo(true)
          .estaVerificado(true)
          .build();
      usuarioRepository.save(adminUsuario);
    }
  }
}