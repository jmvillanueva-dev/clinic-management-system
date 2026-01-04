package com.clinic.webapi.modules.auth.service;

import com.clinic.webapi.modules.auth.entity.Usuario;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

  private final JavaMailSender mailSender;

  @Value("${spring.mail.username}")
  private String fromEmail;

  @Value("${app.frontend-url}")
  private String frontendUrl;

  /**
   * Envía el correo de bienvenida y verificación al nuevo empleado en formato HTML.
   * @param usuario Usuario recién creado.
   * @param contrasenaGenerada Contraseña temporal generada por el sistema.
   * @param token Token de verificación.
   */
  public void enviarCorreoRegistro(Usuario usuario, String contrasenaGenerada, String token) {
    MimeMessage mimeMessage = mailSender.createMimeMessage();
    try {
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setFrom(fromEmail);
        helper.setTo(usuario.getEmail());
        helper.setSubject("¡Bienvenido/a al Centro Médico - Verificación de Cuenta");

        // 1. URL apunta al frontend
        String verificationUrl = frontendUrl + "/auth/verify?token=" + token;

        // 2. Contenido HTML con botón
        String htmlContent = String.format(
            "<div style='font-family: Arial, sans-serif; color: #333;'>" +
            "<h2>¡Bienvenido(a), %s!</h2>" +
            "<p>Ha sido registrado(a) como empleado en nuestro Centro Médico.</p>" +
            "<p>Sus credenciales temporales son:</p>" +
            "<ul style='list-style-type: none; padding: 0;'>" +
            "  <li><strong>Email:</strong> %s</li>" +
            "  <li><strong>Contraseña:</strong> %s</li>" +
            "</ul>" +
            "<p>Para activar su cuenta e iniciar sesión, por favor, haga clic en el siguiente botón:</p>" +
            "<a href='%s' style='background-color: #007bff; color: white; padding: 10px 20px; text-align: center; text-decoration: none; display: inline-block; border-radius: 5px;'>Verificar Cuenta</a>" +
            "<p style='margin-top: 20px;'>Una vez verifique su cuenta, se recomienda cambiar su contraseña en la sección de perfil.</p>" +
            "<hr>" +
            "<p style='font-size: 0.9em; color: #777;'>Si no puede hacer clic en el botón, copie y pegue el siguiente enlace en su navegador:<br>%s</p>" +
            "<p>Atentamente,<br>El equipo del Centro Médico</p>" +
            "</div>",
            usuario.getEmpleado().getNombre() + " " + usuario.getEmpleado().getApellido(),
            usuario.getEmail(),
            contrasenaGenerada,
            verificationUrl,
            verificationUrl
        );

        helper.setText(htmlContent, true); // true indica que el contenido es HTML

        mailSender.send(mimeMessage);
        System.out.println("Correo de registro HTML enviado exitosamente a: " + usuario.getEmail());

    } catch (MessagingException e) {
        System.err.println("ERROR al enviar correo HTML a " + usuario.getEmail() + ": " + e.getMessage());
        throw new RuntimeException("Fallo al enviar el correo de registro/verificación.");
    }
  }

  /**
   * Envía el correo de recuperación de contraseña en formato HTML.
   * @param usuario El usuario que solicita la recuperación.
   * @param token El token único de recuperación.
   */
  public void enviarCorreoRecuperacion(Usuario usuario, String token) {
      MimeMessage mimeMessage = mailSender.createMimeMessage();
      try {
          MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

          helper.setFrom(fromEmail);
          helper.setTo(usuario.getEmail());
          helper.setSubject("Recuperación de Contraseña - Centro Médico");

          String resetUrl = frontendUrl + "/auth/reset-password?token=" + token;

          String htmlContent = String.format(
              "<div style='font-family: Arial, sans-serif; color: #333;'>" +
              "<h2>Recuperación de Contraseña</h2>" +
              "<p>Estimado(a) %s,</p>" +
              "<p>Hemos recibido una solicitud para restablecer tu contraseña. Para continuar, haz clic en el siguiente botón:</p>" +
              "<a href='%s' style='background-color: #007bff; color: white; padding: 10px 20px; text-align: center; text-decoration: none; display: inline-block; border-radius: 5px;'>Restablecer Contraseña</a>" +
              "<p style='margin-top: 20px;'>Este enlace expirará en 15 minutos.</p>" +
              "<p>Si no solicitaste este cambio, puedes ignorar este correo.</p>" +
              "<hr>" +
              "<p style='font-size: 0.9em; color: #777;'>Si no puede hacer clic en el botón, copie y pegue el siguiente enlace en su navegador:<br>%s</p>" +
              "<p>Atentamente,<br>El equipo del Centro Médico</p>" +
              "</div>",
              usuario.getEmpleado().getNombre(),
              resetUrl,
              resetUrl
          );

          helper.setText(htmlContent, true);

          mailSender.send(mimeMessage);
      } catch (MessagingException e) {
          throw new RuntimeException("Error al enviar el correo de recuperación.");
      }
  }
}
