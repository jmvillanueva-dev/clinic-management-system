package com.clinic.webapi.modules.auth.repository;

import com.clinic.webapi.modules.auth.entity.RefreshToken;
import com.clinic.webapi.modules.auth.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUsuario(Usuario usuario);

    void deleteByUsuario(Usuario usuario);
}
