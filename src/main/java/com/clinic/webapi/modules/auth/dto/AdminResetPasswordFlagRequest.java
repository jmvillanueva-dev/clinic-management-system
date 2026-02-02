package com.clinic.webapi.modules.auth.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * DTO para que un administrador fuerce el cambio de contraseña de un usuario.
 */
@Data
public class AdminResetPasswordFlagRequest {

  @NotNull(message = "El ID del empleado es obligatorio.")
  private UUID empleadoId;
}
