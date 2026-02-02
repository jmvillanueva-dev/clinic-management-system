package com.clinic.webapi.modules.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la respuesta del cambio obligatorio de contraseña.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForcePasswordChangeResponse {

  private String mensaje;
  private boolean success;
  private String email;
}
