  package com.clinic.webapi.modules.catalogos.model.mapper;

  import com.clinic.webapi.modules.catalogos.dto.CatalogoRequest;
  import com.clinic.webapi.modules.catalogos.dto.CatalogoResponse;
  import com.clinic.webapi.modules.catalogos.dto.ItemCatalogoResponse;
  import com.clinic.webapi.modules.catalogos.model.entity.Catalogo;
  import org.mapstruct.*;

  import java.util.List;
  import java.util.stream.Collectors;

  @Mapper(componentModel = "spring")
  public interface CatalogoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "fechaActualizacion", ignore = true)
    @Mapping(target = "items", ignore = true)
    Catalogo toEntity(CatalogoRequest request);

    @Mapping(target = "items", source = "items", qualifiedByName = "mapItemsToResponse")
    CatalogoResponse toResponse(Catalogo catalogo);

    @Named("mapItemsToResponse")
    default List<ItemCatalogoResponse> mapItemsToResponse(List<com.clinic.webapi.modules.catalogos.model.entity.ItemCatalogo> items) {
      if (items == null) {
        return null;
      }
      return items.stream()
          .map(this::mapItemToResponse)
          .collect(Collectors.toList());
    }

    default ItemCatalogoResponse mapItemToResponse(com.clinic.webapi.modules.catalogos.model.entity.ItemCatalogo item) {
      if (item == null) {
        return null;
      }
      return ItemCatalogoResponse.builder()
          .id(item.getId())
          .catalogoId(item.getCatalogo() != null ? item.getCatalogo().getId() : null)
          .catalogoNombre(item.getCatalogo() != null ? item.getCatalogo().getNombre() : null)
          .nombre(item.getNombre())
          .descripcion(item.getDescripcion())
          .codigo(item.getCodigo())
          .valor(item.getValor())
          .estaActivo(item.isEstaActivo())
          .fechaCreacion(item.getFechaCreacion())
          .fechaActualizacion(item.getFechaActualizacion())
          .build();
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "fechaActualizacion", ignore = true)
    @Mapping(target = "items", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Catalogo updateEntityFromRequest(@MappingTarget Catalogo catalogo, CatalogoRequest request);
  }