package com.clinic.webapi.modules.catalogos.repository;

import com.clinic.webapi.modules.catalogos.model.entity.Catalogo;
import com.clinic.webapi.modules.catalogos.model.entity.ItemCatalogo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ItemCatalogoRepository extends JpaRepository<ItemCatalogo, UUID> {

  Optional<ItemCatalogo> findByCatalogoAndNombre(Catalogo catalogo, String nombre);

  boolean existsByCatalogoAndNombre(Catalogo catalogo, String nombre);

  List<ItemCatalogo> findByCatalogo(Catalogo catalogo);

  List<ItemCatalogo> findByCatalogoAndEstaActivo(Catalogo catalogo, boolean estaActivo);

  @Query("SELECT i FROM ItemCatalogo i WHERE i.catalogo.tipo = :tipo AND i.estaActivo = true")
  List<ItemCatalogo> findByTipoCatalogo(@Param("tipo") String tipo);

  @Query("SELECT i FROM ItemCatalogo i WHERE i.catalogo.nombre = :catalogoNombre AND i.estaActivo = true")
  List<ItemCatalogo> findByNombreCatalogo(@Param("catalogoNombre") String catalogoNombre);
}