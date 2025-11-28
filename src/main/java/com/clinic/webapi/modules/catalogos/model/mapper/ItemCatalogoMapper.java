package com.clinic.webapi.modules.catalogos.model.mapper;

import com.clinic.webapi.modules.catalogos.dto.ItemCatalogoMinifiedResponse; // Importar nuevo DTO
import com.clinic.webapi.modules.catalogos.dto.ItemCatalogoRequest;
import com.clinic.webapi.modules.catalogos.dto.ItemCatalogoResponse;
import com.clinic.webapi.modules.catalogos.model.entity.Catalogo;
import com.clinic.webapi.modules.catalogos.model.entity.ItemCatalogo;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ItemCatalogoMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "catalogo", source = "catalogo")
  @Mapping(target = "nombre", source = "request.nombre")
  @Mapping(target = "descripcion", source = "request.descripcion")
  @Mapping(target = "codigo", source = "request.codigo")
  @Mapping(target = "valor", source = "request.valor")
  @Mapping(target = "estaActivo", source = "request.estaActivo")
  @Mapping(target = "fechaCreacion", ignore = true)
  @Mapping(target = "fechaActualizacion", ignore = true)
  ItemCatalogo toEntity(ItemCatalogoRequest request, Catalogo catalogo);

  @Mapping(target = "catalogoId", source = "catalogo.id")
  @Mapping(target = "catalogoNombre", source = "catalogo.nombre")
  ItemCatalogoResponse toResponse(ItemCatalogo itemCatalogo);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "catalogo", source = "catalogo")
  @Mapping(target = "nombre", source = "request.nombre")
  @Mapping(target = "descripcion", source = "request.descripcion")
  @Mapping(target = "codigo", source = "request.codigo")
  @Mapping(target = "valor", source = "request.valor")
  @Mapping(target = "estaActivo", source = "request.estaActivo")
  @Mapping(target = "fechaCreacion", ignore = true)
  @Mapping(target = "fechaActualizacion", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  ItemCatalogo updateEntityFromRequest(@MappingTarget ItemCatalogo itemCatalogo, ItemCatalogoRequest request, Catalogo catalogo);

  // --- NUEVO MÉTODO ---
  @Mapping(target = "catalogoId", source = "catalogo.id")
  @Mapping(target = "catalogoNombre", source = "catalogo.nombre")
  ItemCatalogoMinifiedResponse toMinifiedResponse(ItemCatalogo itemCatalogo);
}
