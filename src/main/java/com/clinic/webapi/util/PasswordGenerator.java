package com.clinic.webapi.util;

import org.springframework.stereotype.Component;
import java.security.SecureRandom;
import java.util.UUID;

@Component
public class PasswordGenerator {

  private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
  private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
  private static final String NUMBER = "0123456789";
  private static final String OTHER_CHAR = "!@#$%&*_";
  private static final String PASSWORD_ALLOW_BASE = CHAR_LOWER + CHAR_UPPER + NUMBER + OTHER_CHAR;
  private static final SecureRandom random = new SecureRandom();

  public String generarContrasenaTemporal(int length) {
    StringBuilder sb = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      int randomCharIndex = random.nextInt(PASSWORD_ALLOW_BASE.length());
      sb.append(PASSWORD_ALLOW_BASE.charAt(randomCharIndex));
    }
    return sb.toString();
  }

  public String generarTokenVerificacion() {
    return UUID.randomUUID().toString();
  }
}