package com.clinic.webapi.modules.auth.controller;

import com.clinic.webapi.modules.auth.dto.ForgotPasswordRequest;
import com.clinic.webapi.modules.auth.dto.ForcePasswordChangeRequest;
import com.clinic.webapi.modules.auth.dto.ForcePasswordChangeResponse;
import com.clinic.webapi.modules.auth.dto.ResetPasswordRequest;
import com.clinic.webapi.modules.auth.dto.AdminResetPasswordFlagRequest;
import com.clinic.webapi.modules.auth.entity.RefreshToken;
import com.clinic.webapi.modules.auth.service.RefreshTokenService;
import com.clinic.webapi.shared.dto.ApiResponse;
import com.clinic.webapi.modules.auth.dto.AuthRequest;
import com.clinic.webapi.modules.auth.dto.AuthResponse;
import com.clinic.webapi.modules.auth.dto.RegisterRequest;
import com.clinic.webapi.modules.auth.dto.RegisterResponse;
import com.clinic.webapi.modules.auth.dto.TokenRefreshRequest;
import com.clinic.webapi.modules.auth.dto.TokenRefreshResponse;
import com.clinic.webapi.modules.empleados.entity.Empleado;
import com.clinic.webapi.modules.empleados.entity.Rol;
import com.clinic.webapi.modules.auth.entity.Usuario;
import com.clinic.webapi.shared.security.JwtService;
import com.clinic.webapi.modules.auth.service.UserService;
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
  private final RefreshTokenService refreshTokenService;
  /**
   * Endpoint para registrar nuevos empleados/usuarios.
   * Solo accesible por usuarios con el rol 'ADMINISTRADOR'.
   */
  @PreAuthorize("hasRole('ADMINISTRADOR')")
  @PostMapping("/register")
  public ResponseEntity<ApiResponse<RegisterResponse>> register(@Valid @RequestBody RegisterRequest request) {
    try {
      RegisterResponse response = userService.registerUser(request);
      return ResponseEntity.ok(ApiResponse.success("Usuario registrado exitosamente", response));
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest()
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  /**
   * Endpoint para verificar cuenta nuevos empleados/usuarios.
   */
  @PostMapping("/verify/{token}")
  public ResponseEntity<ApiResponse<Void>> verifyAccount(@PathVariable String token) {
    try {
      userService.verifyUserAccount(token);
      return ResponseEntity.ok(ApiResponse.success("Cuenta verificada exitosamente. Ahora puede iniciar sesión."));
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest()
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  /**
   * Endpoint para el inicio de sesión.
   */
  @PostMapping("/login")
  public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody AuthRequest request) {
    try {
      // 1. Intenta autenticar
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

      String accessToken = jwtService.generateToken(usuario.getId(), usuario.getEmail(), roles);
      RefreshToken refreshToken = refreshTokenService.createRefreshToken(usuario.getId());
      Empleado empleado = usuario.getEmpleado();

      AuthResponse response = AuthResponse.builder()
          .accessToken(accessToken)
          .refreshToken(refreshToken.getToken())
          .tokenType("Bearer")
          .email(usuario.getEmail())
          .roles(roles)
          .employeeId(empleado.getId())
          .name(empleado.getNombre())
          .lastName(empleado.getApellido())
          .requiereCambioPassword(usuario.isRequiereCambioPassword())
          .build();

      return ResponseEntity.ok(ApiResponse.success("Inicio de sesión exitoso", response));

    } catch (UsernameNotFoundException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(ApiResponse.error("Usuario no registrado. Por favor, solicite el registro a un administrador."));

    } catch (BadCredentialsException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(ApiResponse.error("Credenciales incorrectas. Verifique su email y contraseña."));

    } catch (DisabledException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(ApiResponse.error(e.getMessage()));

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(ApiResponse.error("Error en el proceso de login: " + e.getMessage()));
    }
  }

  @PostMapping("/refresh-token")
  public ResponseEntity<ApiResponse<TokenRefreshResponse>> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
      String requestRefreshToken = request.getRefreshToken();

      return refreshTokenService.findByToken(requestRefreshToken)
          .map(refreshTokenService::verifyExpiration)
          .map(RefreshToken::getUsuario)
          .map(usuario -> {
              Set<String> roles = usuario.getRoles().stream().map(Rol::getNombre).collect(Collectors.toSet());
              String accessToken = jwtService.generateToken(usuario.getId(), usuario.getEmail(), roles);
              
              return ResponseEntity.ok(ApiResponse.success("Token refrescado exitosamente", 
                  TokenRefreshResponse.builder()
                      .accessToken(accessToken)
                      .refreshToken(requestRefreshToken)
                      .build()));
          })
          .orElseThrow(() -> new RuntimeException("El refresh token no existe en la base de datos!"));
  }

  @PostMapping("/forgot-password")
  public ResponseEntity<ApiResponse<Void>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
    try {
      userService.solicitarRecuperacionPassword(request);
      return ResponseEntity.ok(ApiResponse.success("Se ha enviado un correo con instrucciones para restablecer tu contraseña."));
    } catch (RuntimeException e) {
      // Por seguridad, a veces es mejor responder OK aunque el email no exista para no revelar usuarios,
      // pero para desarrollo dejaremos el mensaje de error.
      return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
    }
  }

  @PostMapping("/reset-password")
  public ResponseEntity<ApiResponse<Void>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
    try {
      userService.restablecerPassword(request);
      return ResponseEntity.ok(ApiResponse.success("Contraseña actualizada exitosamente. Ahora puedes iniciar sesión."));
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
    }
  }

  /**
   * Endpoint para cambio obligatorio de contraseña.
   * Se utiliza cuando un usuario nuevo debe cambiar su contraseña temporal
   * en su primer inicio de sesión.
   */
  @PostMapping("/force-password-change")
  public ResponseEntity<ApiResponse<ForcePasswordChangeResponse>> forcePasswordChange(
      @Valid @RequestBody ForcePasswordChangeRequest request) {
    try {
      ForcePasswordChangeResponse response = userService.cambiarPasswordObligatorio(request);
      return ResponseEntity.ok(ApiResponse.success("Contraseña actualizada exitosamente.", response));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
    }
  }

  /**
   * Endpoint para que un administrador fuerce el cambio de contraseña de un usuario.
   * Esto establece el flag requiereCambioPassword = true para el usuario especificado.
   */
  @PreAuthorize("hasRole('ADMINISTRADOR')")
  @PostMapping("/admin/force-password-reset")
  public ResponseEntity<ApiResponse<Void>> adminForcePasswordReset(
      @Valid @RequestBody AdminResetPasswordFlagRequest request) {
    try {
      userService.forzarCambioPasswordPorAdmin(request.getEmpleadoId());
      return ResponseEntity.ok(ApiResponse.success(
          "Se ha establecido que el usuario debe cambiar su contraseña en el próximo inicio de sesión."));
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
    }
  }
}
