package com.clinic.webapi.modules.catalogos.repository;

import com.clinic.webapi.modules.catalogos.model.entity.Catalogo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CatalogoRepository extends JpaRepository<Catalogo, UUID> {

  Optional<Catalogo> findByNombre(String nombre);

  boolean existsByNombre(String nombre);

  List<Catalogo> findByTipo(String tipo);

  List<Catalogo> findByEstaActivo(boolean estaActivo);

  @Query("SELECT c FROM Catalogo c LEFT JOIN FETCH c.items WHERE c.id = :id")
  Optional<Catalogo> findByIdWithItems(@Param("id") UUID id);

  // --- NUEVO MÉTODO ---
  // Traemos todos los catálogos activos y hacemos un JOIN FETCH para traer sus items de una vez.
  @Query("SELECT DISTINCT c FROM Catalogo c LEFT JOIN FETCH c.items i WHERE c.estaActivo = true AND (i IS NULL OR i.estaActivo = true)")
  List<Catalogo> findAllActiveWithItems();
}
