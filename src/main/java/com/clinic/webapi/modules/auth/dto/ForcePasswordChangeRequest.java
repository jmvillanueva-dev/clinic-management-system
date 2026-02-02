package com.clinic.webapi.modules.auth.dto;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO para la solicitud de cambio obligatorio de contraseña.
 * Se utiliza cuando un usuario nuevo debe cambiar su contraseña temporal
 * en su primer inicio de sesión.
 */
@Data
public class ForcePasswordChangeRequest {

  @NotBlank(message = "El email es obligatorio.")
  @Email(message = "El formato del email no es válido.")
  private String email;

  @NotBlank(message = "La contraseña actual es obligatoria.")
  private String contrasenaActual;

  @NotBlank(message = "La nueva contraseña es obligatoria.")
  @Size(min = 8, message = "La nueva contraseña debe tener al menos 8 caracteres.")
  @Pattern(
      regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#+_\\-])[A-Za-z\\d@$!%*?&#+_\\-]{8,}$",
      message = "La contraseña debe contener al menos: una letra minúscula, una letra mayúscula, un número y un carácter especial (@$!%*?&#+_-)."
  )
  private String nuevaContrasena;
}
