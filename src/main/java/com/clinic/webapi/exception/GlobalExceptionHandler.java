package com.clinic.webapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Maneja la excepción lanzada por @Valid cuando la validación falla.
   * Convierte los errores de campo en un mapa legible para el frontend.
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {

    Map<String, String> errors = new HashMap<>();

    // Recorre todos los errores de campo y mapea el nombre del campo con su mensaje de error
    ex.getBindingResult().getFieldErrors().forEach(error -> {
      String fieldName = error.getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });

    // Retorna un código 400 Bad Request junto con el mapa de errores
    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }

  /**
   * Maneja la excepción lanzada por Spring Security cuando falla la autorización (@PreAuthorize).
   * Devuelve 403 Forbidden con un mensaje claro.
   */
  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
    // Mensaje específico para el frontend
    String message = "Acceso denegado. No está autorizado para realizar esta acción. " +
        "Se requiere el rol ADMINISTRADOR.";

    return new ResponseEntity<>(message, HttpStatus.FORBIDDEN);
  }

  /**
   * Maneja excepciones genéricas de negocio (ej: RuntimeException de servicios)
   */
  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<String> handleRuntimeExceptions(RuntimeException ex) {
    // Captura excepciones lanzadas en el servicio (ej: "El email ya está registrado.")
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }
}