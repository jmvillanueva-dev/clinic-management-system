package com.clinic.webapi.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
  private boolean success;
  private String message;
  private T data;
  private Instant timestamp;
  private String path;

  // Métodos estáticos para crear respuestas exitosas
  public static <T> ApiResponse<T> success(T data) {
    return ApiResponse.<T>builder()
        .success(true)
        .data(data)
        .timestamp(Instant.now())
        .build();
  }

  public static <T> ApiResponse<T> success(String message, T data) {
    return ApiResponse.<T>builder()
        .success(true)
        .message(message)
        .data(data)
        .timestamp(Instant.now())
        .build();
  }

  public static ApiResponse<Void> success(String message) {
    return ApiResponse.<Void>builder()
        .success(true)
        .message(message)
        .timestamp(Instant.now())
        .build();
  }

  // Métodos estáticos para crear respuestas de error
  public static <T> ApiResponse<T> error(String message) {
    return ApiResponse.<T>builder()
        .success(false)
        .message(message)
        .timestamp(Instant.now())
        .build();
  }

  public static <T> ApiResponse<T> error(String message, T data) {
    return ApiResponse.<T>builder()
        .success(false)
        .message(message)
        .data(data)
        .timestamp(Instant.now())
        .build();
  }
}