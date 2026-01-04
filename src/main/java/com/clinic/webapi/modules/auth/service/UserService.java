package com.clinic.webapi.modules.auth.service;

import com.clinic.webapi.modules.auth.dto.ForgotPasswordRequest;
import com.clinic.webapi.modules.auth.dto.ResetPasswordRequest;
import com.clinic.webapi.modules.empleados.dto.EmpleadoUpdateRequest;
import com.clinic.webapi.modules.auth.dto.UsuarioEmailUpdateRequest;
import com.clinic.webapi.modules.auth.dto.UsuarioPasswordUpdateRequest;
import com.clinic.webapi.modules.auth.dto.RegisterRequest;
import com.clinic.webapi.modules.auth.dto.RegisterResponse;
import com.clinic.webapi.modules.empleados.entity.Empleado;
import com.clinic.webapi.modules.empleados.entity.Rol;
import com.clinic.webapi.modules.auth.entity.Usuario;
import com.clinic.webapi.modules.empleados.repository.EmpleadoRepository;
import com.clinic.webapi.modules.empleados.repository.RolRepository;
import com.clinic.webapi.modules.auth.repository.UsuarioRepository;
import com.clinic.webapi.shared.util.PasswordGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UsuarioRepository usuarioRepository;
  private final RolRepository rolRepository;
  private final EmpleadoRepository empleadoRepository;
  private final PasswordEncoder passwordEncoder;
  private final EmailService emailService;
  private final PasswordGenerator passwordGenerator;

  private static final String ADMIN_ROLE_NAME = "ADMINISTRADOR";
  private static final int TEMP_PASS_LENGTH = 12;

  // Metodo auxiliar para validar si el usuario tiene rol de ADMIN
  private boolean isAdmin(Usuario usuario) {
    return usuario.getRoles().stream()
        .anyMatch(rol -> rol.getNombre().toUpperCase().equals(ADMIN_ROLE_NAME));
  }

  // Obtener empleado por ID
  public Empleado findEmpleadoById(UUID id) {
    return empleadoRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + id));
  }

  // Obtener todos los empleados activos
  public List<Empleado> findAllActiveEmpleados() {
    return empleadoRepository.findAllByEstaActivo(true);
  }

  // --- 1. CRUD de Empleado y Actualización de Email de Usuario (Compartido por ADMIN) ---

  @Transactional
  public Empleado actualizarEmpleado(UUID idEmpleado, EmpleadoUpdateRequest request, UUID userId) {
    Usuario usuarioSolicitante = usuarioRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("Usuario solicitante no encontrado."));

    Empleado empleadoExistente = findEmpleadoById(idEmpleado);

    // 1. Validar permisos: ADMIN o el propio empleado
    boolean esAdmin = isAdmin(usuarioSolicitante);
    boolean esMismoEmpleado = empleadoExistente.getId().equals(usuarioSolicitante.getEmpleado().getId());

    if (!esAdmin && !esMismoEmpleado) {
      throw new SecurityException("No tiene permisos para actualizar los datos de este empleado.");
    }

    // 2. Aplicar cambios (los campos nulos se ignoran, permitiendo actualizaciones parciales)
    if (request.getNombre() != null) empleadoExistente.setNombre(request.getNombre());
    if (request.getApellido() != null) empleadoExistente.setApellido(request.getApellido());

    // Validar unicidad de cédula si se cambia
    if (request.getCedula() != null && !request.getCedula().equals(empleadoExistente.getCedula())) {
      if (empleadoRepository.existsByCedula(request.getCedula())) {
        throw new RuntimeException("La cédula ya está registrada.");
      }
      empleadoExistente.setCedula(request.getCedula());
    }

    if (request.getEspecialidad() != null) empleadoExistente.setEspecialidad(request.getEspecialidad());
    if (request.getCodigoProfesional() != null) empleadoExistente.setCodigoProfesional(request.getCodigoProfesional());
    if (request.getTelefono() != null) empleadoExistente.setTelefono(request.getTelefono());

    // 3. Actualizar roles si se proporcionan y el solicitante es ADMIN
    if (request.getRoles() != null && !request.getRoles().isEmpty()) {
      if (!esAdmin) {
        throw new SecurityException("Solo un administrador puede modificar los roles.");
      }

      Usuario usuarioAsociado = usuarioRepository.findByEmpleado(empleadoExistente)
          .orElseThrow(() -> new RuntimeException("Usuario asociado al empleado no encontrado."));

      Set<Rol> nuevosRoles = request.getRoles().stream()
          .map(rolName -> rolRepository.findByNombre(rolName)
              .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + rolName)))
          .collect(Collectors.toSet());

      usuarioAsociado.setRoles(nuevosRoles);
      usuarioRepository.save(usuarioAsociado);
    }

    return empleadoRepository.save(empleadoExistente);
  }

  // Metodo para deshabilitar lógicamente un empleado
  @Transactional
  public void eliminarEmpleado(UUID idEmpleado, UUID userId) {
    Usuario usuarioSolicitante = usuarioRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("Usuario solicitante no encontrado."));

    // Solo ADMIN puede "eliminar" (desactivar) perfiles
    if (!isAdmin(usuarioSolicitante)) {
      throw new SecurityException("Solo un administrador puede desactivar perfiles de empleados.");
    }

    Empleado empleadoExistente = findEmpleadoById(idEmpleado);

    // Restricción: Un ADMIN no puede desactivarse a sí mismo
    if (empleadoExistente.getId().equals(usuarioSolicitante.getEmpleado().getId())) {
      throw new SecurityException("No se puede desactivar su propio perfil de empleado.");
    }

    // Desactivación lógica
    empleadoExistente.setEstaActivo(false);
    empleadoRepository.save(empleadoExistente);

    // Desactivar todos los usuarios asociados a este Empleado
    List<Usuario> usuariosAsociados = usuarioRepository.findAllByEmpleado(empleadoExistente);

    for (Usuario u : usuariosAsociados) {
      u.setEstaActivo(false);
      usuarioRepository.save(u);
    }
  }

  // --- 2. Actualización de Email (Para ADMIN o Empleado) ---
  @Transactional
  public Usuario actualizarUsuarioEmail(UUID idEmpleado, UsuarioEmailUpdateRequest request, UUID userId) {
    Usuario usuarioSolicitante = usuarioRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("Usuario solicitante no encontrado."));

    Empleado empleadoObjetivo = findEmpleadoById(idEmpleado);

    // Obtener el usuario asociado al empleado
    Usuario usuarioObjetivo = usuarioRepository.findByEmpleado(empleadoObjetivo)
        .orElseThrow(() -> new RuntimeException("Usuario asociado no encontrado."));

    // Validar permisos
    boolean esAdmin = isAdmin(usuarioSolicitante);
    boolean esMismoUsuario = usuarioObjetivo.getId().equals(usuarioSolicitante.getId());

    if (!esAdmin && !esMismoUsuario) {
      throw new SecurityException("No tiene permisos para actualizar el email de este usuario.");
    }

    // Validar unicidad del nuevo email
    if (usuarioRepository.findByEmail(request.getNuevoEmail()).isPresent() &&
        !request.getNuevoEmail().equalsIgnoreCase(usuarioObjetivo.getEmail())) {
      throw new RuntimeException("El nuevo email ya está en uso.");
    }

    usuarioObjetivo.setEmail(request.getNuevoEmail());
    return usuarioRepository.save(usuarioObjetivo);
  }

  // --- 3. Actualización de Contraseña (Solo Empleado logueado) ---
  @Transactional
  public Usuario actualizarUsuarioPassword(UsuarioPasswordUpdateRequest request, UUID userId) {
    Usuario usuarioExistente = usuarioRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("Usuario logueado no encontrado."));

    // 1. Validar contraseña actual
    if (!passwordEncoder.matches(request.getContrasenaActual(), usuarioExistente.getPasswordHash())) {
      throw new IllegalArgumentException("La contraseña actual es incorrecta.");
    }

    // 2. Aplicar nueva contraseña
    usuarioExistente.setPasswordHash(passwordEncoder.encode(request.getNuevaContrasena()));
    return usuarioRepository.save(usuarioExistente);
  }

  // Registrar un usuario
  @Transactional
  public RegisterResponse registerUser(RegisterRequest request) {
    // 1. Validar que el email y la cédula no existan
    if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
      throw new RuntimeException("El email ya está registrado.");
    }
    if (empleadoRepository.existsByCedula(request.getCedula())) {
      throw new RuntimeException("La cédula ya está registrada.");
    }

    String contrasenaTemporal = passwordGenerator.generarContrasenaTemporal(TEMP_PASS_LENGTH);
    String tokenVerificacion = passwordGenerator.generarTokenVerificacion();

    // 2. Crear y guardar Empleado
    Empleado empleado = Empleado.builder()
        .nombre(request.getNombre())
        .apellido(request.getApellido())
        .cedula(request.getCedula())
        .especialidad(request.getEspecialidad())
        .codigoProfesional(request.getCodigoProfesional())
        .telefono(request.getTelefono())
        .estaActivo(true)
        .build();
    empleado = empleadoRepository.save(empleado);

    // 3. Obtener los Roles
    Set<Rol> roles = request.getRoles().stream()
        .map(rolName -> rolRepository.findByNombre(rolName)
            .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + rolName)))
        .collect(Collectors.toSet());

    // 4. Crear y guardar Usuario
    Usuario usuario = Usuario.builder()
        .email(request.getEmail())
        .passwordHash(passwordEncoder.encode(contrasenaTemporal))
        .empleado(empleado)
        .estaActivo(true)
        .estaVerificado(false)
        .tokenVerificacion(tokenVerificacion)
        .roles(roles)
        .build();
    usuario = usuarioRepository.save(usuario);

    // 5. Enviar Correo de Notificacion
    emailService.enviarCorreoRegistro(usuario, contrasenaTemporal, tokenVerificacion);

    // 6. Devolver RegisterResponse
    return RegisterResponse.builder()
        .idUsuario(usuario.getId())
        .email(usuario.getEmail())
        .estaActivo(usuario.isEstaActivo())
        .estaVerificado(usuario.isEstaVerificado())
        .fechaCreacion(usuario.getFechaCreacion())
        .roles(roles.stream().map(Rol::getNombre).collect(Collectors.toSet()))
        .idEmpleado(empleado.getId())
        .nombre(empleado.getNombre())
        .apellido(empleado.getApellido())
        .cedula(empleado.getCedula())
        .especialidad(empleado.getEspecialidad())
        .build();
  }

  public Optional<Usuario> findByEmail(String email) {
    return usuarioRepository.findByEmail(email);
  }

  public Optional<Usuario> findByEmpleado(Empleado empleado) {
    return usuarioRepository.findByEmpleado(empleado);
  }


  // Metodo para validar si un usuario autenticado puedo acceder al ID indicado
  public boolean canAccessEmpleadoById(String userIdString, UUID empleadoId) {
    if (userIdString == null) return false;

    UUID userId;
    try {
      userId = UUID.fromString(userIdString);
    } catch (IllegalArgumentException e) {
      return false;
    }

    Optional<Usuario> usuarioOpt = usuarioRepository.findById(userId);
    if (usuarioOpt.isEmpty()) return false;

    Usuario usuario = usuarioOpt.get();

    // 1. Si es ADMIN, siempre puede acceder
    if (isAdmin(usuario)) return true;

    // 2. Si no es ADMIN, solo puede acceder a su propio perfil
    return usuario.getEmpleado() != null && usuario.getEmpleado().getId().equals(empleadoId);
  }

  // Metodo para validar el token y actualizar su estado
  @Transactional
  public void verifyUserAccount(String token) {
    Usuario usuario = usuarioRepository.findByTokenVerificacion(token)
        .orElseThrow(() -> new RuntimeException("Token de verificación inválido o expirado."));

    if (usuario.isEstaVerificado()) {
      throw new RuntimeException("Esta cuenta ya ha sido verificada.");
    }

    usuario.setEstaVerificado(true);
    usuario.setTokenVerificacion(null);
    usuarioRepository.save(usuario);
  }

  // 1. Solicitar recuperación (Generar token y enviar email)
  @Transactional
  public void solicitarRecuperacionPassword(ForgotPasswordRequest request) {
    Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new RuntimeException("No existe un usuario registrado con ese email."));

    // Generar token (usamos UUID random)
    String token = UUID.randomUUID().toString();

    // Configurar expiración (ej: 15 minutos)
    usuario.setTokenRecuperacion(token);
    usuario.setFechaExpiracionRecuperacion(Instant.now().plus(15, ChronoUnit.MINUTES));

    usuarioRepository.save(usuario);

    // Enviar correo
    emailService.enviarCorreoRecuperacion(usuario, token);
  }

  // 2. Restablecer contraseña (Validar token y cambiar password)
  @Transactional
  public void restablecerPassword(ResetPasswordRequest request) {
    Usuario usuario = usuarioRepository.findByTokenRecuperacion(request.getToken())
        .orElseThrow(() -> new RuntimeException("El token de recuperación es inválido."));

    // Validar expiración
    if (usuario.getFechaExpiracionRecuperacion().isBefore(Instant.now())) {
      throw new RuntimeException("El token de recuperación ha expirado. Solicita uno nuevo.");
    }

    // Actualizar contraseña
    usuario.setPasswordHash(passwordEncoder.encode(request.getNuevaPassword()));

    // Limpiar token para que no pueda usarse de nuevo
    usuario.setTokenRecuperacion(null);
    usuario.setFechaExpiracionRecuperacion(null);

    usuarioRepository.save(usuario);
  }
}
