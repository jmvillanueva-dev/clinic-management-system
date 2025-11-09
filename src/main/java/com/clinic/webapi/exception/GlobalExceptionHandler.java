package com.clinic.webapi.exception;

import com.clinic.webapi.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
    Map<String, String> errors = new HashMap<>();

    ex.getBindingResult().getFieldErrors().forEach(error -> {
      String fieldName = error.getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });

    ApiResponse<Map<String, String>> response = ApiResponse.<Map<String, String>>builder()
        .success(false)
        .message("Error de validación en los datos enviados")
        .data(errors)
        .timestamp(java.time.Instant.now())
        .path(request.getDescription(false).replace("uri=", ""))
        .build();

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ApiResponse<Void>> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
    String message = "Acceso denegado. No está autorizado para realizar esta acción.";

    ApiResponse<Void> response = ApiResponse.<Void>builder()
        .success(false)
        .message(message)
        .timestamp(java.time.Instant.now())
        .path(request.getDescription(false).replace("uri=", ""))
        .build();

    return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ApiResponse<Void>> handleRuntimeExceptions(RuntimeException ex, WebRequest request) {
    ApiResponse<Void> response = ApiResponse.<Void>builder()
        .success(false)
        .message(ex.getMessage())
        .timestamp(java.time.Instant.now())
        .path(request.getDescription(false).replace("uri=", ""))
        .build();

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  // Manejo genérico para otras excepciones
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex, WebRequest request) {
    ApiResponse<Void> response = ApiResponse.<Void>builder()
        .success(false)
        .message("Error interno del servidor")
        .timestamp(java.time.Instant.now())
        .path(request.getDescription(false).replace("uri=", ""))
        .build();

    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}