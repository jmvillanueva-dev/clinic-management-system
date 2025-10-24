package com.clinic.webapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class AuthResponse {
  private String accessToken;
  private String tokenType = "Bearer";
  private String email;
  private Set<String> roles;
}