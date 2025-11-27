package com.clinic.webapi.modules.auth.service;

import com.clinic.webapi.modules.auth.entity.RefreshToken;
import com.clinic.webapi.modules.auth.entity.Usuario;
import com.clinic.webapi.modules.auth.repository.RefreshTokenRepository;
import com.clinic.webapi.modules.auth.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

  @Value("${jwt.refresh-expiration-ms:604800000}")
  private Long refreshTokenDurationMs;

  private final RefreshTokenRepository refreshTokenRepository;
  private final UsuarioRepository usuarioRepository;

  public Optional<RefreshToken> findByToken(String token) {
    return refreshTokenRepository.findByToken(token);
  }

  @Transactional
  public RefreshToken createRefreshToken(UUID userId) {
    Usuario usuario = usuarioRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    // Buscamos si ya existe un token para este usuario
    Optional<RefreshToken> existingToken = refreshTokenRepository.findByUsuario(usuario);

    RefreshToken refreshToken;

    if (existingToken.isPresent()) {
        // (UPSERT) Actualizamos sus valores. Esto evita el error de llave duplicada.
        refreshToken = existingToken.get();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        // Al ser una entidad gestionada y estar en @Transactional, el save actualiza
    } else {
        // Crear un nuevo token
        refreshToken = RefreshToken.builder()
            .usuario(usuario)
            .expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))
            .token(UUID.randomUUID().toString())
            .build();
    }

    return refreshTokenRepository.save(refreshToken);
  }

  public RefreshToken verifyExpiration(RefreshToken token) {
    if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
      refreshTokenRepository.delete(token);
      throw new RuntimeException("La sesión ha expirado. Por favor inicie sesión nuevamente.");
    }
    return token;
  }

  @Transactional
  public void deleteByUserId(UUID userId) {
    var usuario = usuarioRepository.findById(userId).get();
    refreshTokenRepository.deleteByUsuario(usuario);
  }
}
