package com.clinic.webapi.modules.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
  private String accessToken;
  private String refreshToken;
  private String tokenType;
  private String email;
  private Set<String> roles;
  private UUID employeeId;
  private String name;
  private String lastName;
  private boolean requiereCambioPassword;
}