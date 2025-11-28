package com.clinic.webapi.modules.catalogos.service;

import com.clinic.webapi.modules.catalogos.dto.CatalogoRequest;
import com.clinic.webapi.modules.catalogos.dto.CatalogoResponse;
import com.clinic.webapi.modules.catalogos.dto.ItemCatalogoMinifiedResponse;
import com.clinic.webapi.modules.catalogos.model.entity.Catalogo;
import com.clinic.webapi.modules.catalogos.model.mapper.CatalogoMapper;
import com.clinic.webapi.modules.catalogos.model.mapper.ItemCatalogoMapper;
import com.clinic.webapi.modules.catalogos.repository.CatalogoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CatalogoService {

  private final CatalogoRepository catalogoRepository;
  private final CatalogoMapper catalogoMapper;
  private final ItemCatalogoMapper itemCatalogoMapper; // Asegúrate de inyectar este mapper

  @Transactional
  public CatalogoResponse crearCatalogo(CatalogoRequest request) {
    if (catalogoRepository.existsByNombre(request.getNombre())) {
      throw new RuntimeException("Ya existe un catálogo con el nombre: " + request.getNombre());
    }

    Catalogo catalogo = catalogoMapper.toEntity(request);
    Catalogo catalogoGuardado = catalogoRepository.save(catalogo);

    return catalogoMapper.toResponse(catalogoGuardado);
  }

  @Transactional(readOnly = true)
  public List<CatalogoResponse> obtenerTodosLosCatalogos() {
    return catalogoRepository.findAll()
        .stream()
        .map(catalogoMapper::toResponse)
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public CatalogoResponse obtenerCatalogoPorId(UUID id) {
    Catalogo catalogo = catalogoRepository.findByIdWithItems(id)
        .orElseThrow(() -> new RuntimeException("Catálogo no encontrado con ID: " + id));
    return catalogoMapper.toResponse(catalogo);
  }

  @Transactional(readOnly = true)
  public List<CatalogoResponse> obtenerCatalogosPorTipo(String tipo) {
    return catalogoRepository.findByTipo(tipo)
        .stream()
        .map(catalogoMapper::toResponse)
        .collect(Collectors.toList());
  }

  @Transactional
  public CatalogoResponse actualizarCatalogo(UUID id, CatalogoRequest request) {
    Catalogo catalogoExistente = catalogoRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Catálogo no encontrado con ID: " + id));

    if (!request.getNombre().equals(catalogoExistente.getNombre()) &&
        catalogoRepository.existsByNombre(request.getNombre())) {
      throw new RuntimeException("Ya existe un catálogo con el nombre: " + request.getNombre());
    }

    Catalogo catalogoActualizado = catalogoMapper.updateEntityFromRequest(catalogoExistente, request);
    Catalogo catalogoGuardado = catalogoRepository.save(catalogoActualizado);

    return catalogoMapper.toResponse(catalogoGuardado);
  }

  @Transactional
  public void eliminarCatalogo(UUID id) {
    Catalogo catalogo = catalogoRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Catálogo no encontrado con ID: " + id));
    catalogoRepository.delete(catalogo);
  }

  @Transactional(readOnly = true)
  public Map<String, List<ItemCatalogoMinifiedResponse>> obtenerTodosLosCatalogosAgrupados() {
    // 1. Obtenemos la data desde la BD (usando la query optimizada del paso anterior)
    List<Catalogo> catalogos = catalogoRepository.findAllActiveWithItems();
    
    // 2. Preparamos el mapa de respuesta con el nuevo tipo de valor
    Map<String, List<ItemCatalogoMinifiedResponse>> mapaCatalogos = new HashMap<>();

    // 3. Iteramos y transformamos a la versión minificada
    for (Catalogo catalogo : catalogos) {
      if (catalogo.getItems() != null && !catalogo.getItems().isEmpty()) {
          
          // Convertimos cada item de la lista a su versión reducida
          List<ItemCatalogoMinifiedResponse> itemsMinificados = catalogo.getItems().stream()
              .map(itemCatalogoMapper::toMinifiedResponse)
              .collect(Collectors.toList());

          mapaCatalogos.put(catalogo.getNombre(), itemsMinificados);
      }
    }

    return mapaCatalogos;
  }
}
