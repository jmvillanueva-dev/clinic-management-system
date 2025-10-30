package com.clinic.webapi.controller;

import com.clinic.webapi.dto.AuthRequest;
import com.clinic.webapi.dto.AuthResponse;
import com.clinic.webapi.dto.RegisterRequest;
import com.clinic.webapi.dto.RegisterResponse;
import com.clinic.webapi.model.entity.Empleado;
import com.clinic.webapi.model.entity.Rol;
import com.clinic.webapi.model.entity.Usuario;
import com.clinic.webapi.security.JwtService;
import com.clinic.webapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

  private final UserService userService;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  /**
   * Endpoint para registrar nuevos empleados/usuarios.
   * Solo accesible por usuarios con el rol 'ADMINISTRADOR'.
   */
  @PreAuthorize("hasRole('ADMINISTRADOR')")
  @PostMapping("/register")
  public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
    try {
      RegisterResponse response = userService.registerUser(request);
      return new ResponseEntity<>(response, HttpStatus.CREATED);
    } catch (RuntimeException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * Endpoint para verificar cuenta nuevos empleados/usuarios.
   */
  @PostMapping("/verify/{token}")
  public ResponseEntity<String> verifyAccount(@PathVariable String token) {
    try {
      userService.verifyUserAccount(token);
      return ResponseEntity.ok("Cuenta verificada exitosamente. Ahora puede iniciar sesión.");
    } catch (RuntimeException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * Endpoint para el inicio de sesión.
   */
  @PostMapping("/login")
  public ResponseEntity<?> login(@Valid @RequestBody AuthRequest request) { // Devolver ResponseEntity<?>
    try {
      // 1. Intenta autenticar. Si falla, Spring lanzará BadCredentialsException o UsernameNotFoundException
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
      );
      SecurityContextHolder.getContext().setAuthentication(authentication);

      // 2. Si la autenticación es exitosa, buscamos al usuario para generar el token
      Usuario usuario = userService.findByEmail(request.getEmail())
          .orElseThrow(() -> new RuntimeException("Error interno: usuario autenticado no encontrado."));

      Set<String> roles = usuario.getRoles().stream()
          .map(Rol::getNombre)
          .collect(Collectors.toSet());

      String token = jwtService.generateToken(usuario.getId(), usuario.getEmail(), roles);

      Empleado empleado = usuario.getEmpleado();

      AuthResponse response = new AuthResponse(token, "Bearer", usuario.getEmail(), roles, empleado.getId(), empleado.getNombre(), empleado.getApellido());

      return ResponseEntity.ok(response);

    } catch (UsernameNotFoundException e) {
      // Caso 1: Usuario NO REGISTRADO (UsernameNotFoundException del UserDetailsService)
      return ResponseEntity
          .status(HttpStatus.UNAUTHORIZED) // 401
          .body("Usuario no registrado. Por favor, solicite el registro a un administrador.");

    } catch (BadCredentialsException e) {
      // Caso 2: Contraseña INCORRECTA (BadCredentialsException de Spring Security)
      return ResponseEntity
          .status(HttpStatus.UNAUTHORIZED) // 401
          .body("Credenciales incorrectas. Verifique su email y contraseña.");

    } catch (DisabledException e) {
      // Caso 3: Manejo de cuenta no verificada
      return ResponseEntity
          .status(HttpStatus.UNAUTHORIZED) // 401
          .body(e.getMessage());

    } catch (Exception e) {
      // Caso 3: Otros errores inesperados
      return ResponseEntity
          .status(HttpStatus.INTERNAL_SERVER_ERROR) // 500
          .body("Error en el proceso de login: " + e.getMessage());
    }
  }
}