package com.clinic.webapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
public class AuthResponse {
  private String accessToken;
  private String tokenType = "Bearer";
  private String email;
  private Set<String> roles;
  private UUID employeeId;
  private String name;
  private String lastName;
}