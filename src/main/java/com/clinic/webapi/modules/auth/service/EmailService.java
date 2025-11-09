package com.clinic.webapi.modules.auth.service;

import com.clinic.webapi.modules.auth.entity.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

  private final JavaMailSender mailSender;

  // Inyecta el correo del remitente configurado en application.properties
  @Value("${spring.mail.username}")
  private String fromEmail;

  /**
   * Envía el correo de bienvenida y verificación al nuevo empleado.
   * @param usuario Usuario recién creado.
   * @param contrasenaGenerada Contraseña temporal generada por el sistema.
   * @param token Token de verificación.
   */
  public void enviarCorreoRegistro(Usuario usuario, String contrasenaGenerada, String token) {
    SimpleMailMessage message = new SimpleMailMessage();

    // --- Asunto y Destinatarios ---
    message.setFrom(fromEmail);
    message.setTo(usuario.getEmail());
    message.setSubject("¡Bienvenido/a al Centro Médico Urdiales Espinoza - Verificación de Cuenta");

    // --- Cuerpo del Mensaje ---
    String verificationUrl = "http://localhost:8080/api/v1/auth/verify/" + token;

    String emailContent = String.format(
        "Estimado(a) empleado(a) %s,\n\n" +
            "Ha sido registrado(a) como empleado en nuestro Centro Médico.\n\n" +
            "Sus credenciales temporales son:\n" +
            "  Email: %s\n" +
            "  Contraseña: %s\n\n" +
            "Para activar su cuenta e iniciar sesión, por favor, haga clic en el siguiente enlace:\n" +
            "%s\n\n" +
            "Una vez verifique su cuenta, se recomienda cambiar su contraseña en la sección de perfil.\n\n" +
            "Atentamente,\n" +
            "El equipo del Centro Médico",
        usuario.getEmpleado().getNombre() + " " + usuario.getEmpleado().getApellido(),
        usuario.getEmail(),
        contrasenaGenerada,
        verificationUrl
    );

    message.setText(emailContent);

    try {
      mailSender.send(message);
      System.out.println("Correo de registro enviado exitosamente a: " + usuario.getEmail());
    } catch (Exception e) {
      System.err.println("ERROR al enviar correo a " + usuario.getEmail() + ": " + e.getMessage());
      // TODO: para el entorno de producción, registrar el error y devolver un 500.
      throw new RuntimeException("Fallo al enviar el correo de registro/verificación.");
    }
  }
}