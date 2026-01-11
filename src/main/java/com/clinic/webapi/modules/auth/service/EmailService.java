package com.clinic.webapi.modules.auth.service;

import com.clinic.webapi.modules.auth.entity.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

  private final RestTemplate restTemplate;

  @Value("${email.service.url}")
  private String emailServiceUrl;

  @Value("${email.service.api-key}")
  private String apiKey;

  /**
   * Envía el correo de bienvenida y verificación al nuevo empleado usando el microservicio externo.
   * @param usuario Usuario recién creado.
   * @param contrasenaGenerada Contraseña temporal generada por el sistema.
   * @param token Token de verificación.
   */
  public void enviarCorreoRegistro(Usuario usuario, String contrasenaGenerada, String token) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("X-API-Key", apiKey);

    Map<String, Object> body = Map.of(
        "to", usuario.getEmail(),
        "type", "registration",
        "userName", usuario.getEmpleado().getNombre() + " " + usuario.getEmpleado().getApellido(),
        "token", token,
        "email", usuario.getEmail(),
        "temporaryPassword", contrasenaGenerada
    );

    HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

    try {
        restTemplate.postForEntity(
            emailServiceUrl + "/api/v1/emails/send",
            request,
            Map.class
        );
        System.out.println("Correo de registro enviado exitosamente a: " + usuario.getEmail());
    } catch (Exception e) {
        System.err.println("ERROR al enviar correo de registro a " + usuario.getEmail() + ": " + e.getMessage());
        throw new RuntimeException("Fallo al enviar el correo de registro/verificación.");
    }
  }

  /**
   * Envía el correo de recuperación de contraseña usando el microservicio externo.
   * @param usuario El usuario que solicita la recuperación.
   * @param token El token único de recuperación.
   */
  public void enviarCorreoRecuperacion(Usuario usuario, String token) {
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      headers.set("X-API-Key", apiKey);

      Map<String, Object> body = Map.of(
          "to", usuario.getEmail(),
          "type", "password-recovery",
          "userName", usuario.getEmpleado().getNombre(),
          "token", token
      );

      HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

      try {
          restTemplate.postForEntity(
              emailServiceUrl + "/api/v1/emails/send",
              request,
              Map.class
          );
          System.out.println("Correo de recuperación enviado exitosamente a: " + usuario.getEmail());
      } catch (Exception e) {
          System.err.println("ERROR al enviar correo de recuperación a " + usuario.getEmail() + ": " + e.getMessage());
          throw new RuntimeException("Error al enviar el correo de recuperación.");
      }
  }
}
