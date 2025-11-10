package com.clinic.webapi.modules.catalogos.service;

import com.clinic.webapi.modules.catalogos.dto.CatalogoRequest;
import com.clinic.webapi.modules.catalogos.dto.CatalogoResponse;
import com.clinic.webapi.modules.catalogos.model.entity.Catalogo;
import com.clinic.webapi.modules.catalogos.model.mapper.CatalogoMapper;
import com.clinic.webapi.modules.catalogos.repository.CatalogoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CatalogoService {

  private final CatalogoRepository catalogoRepository;
  private final CatalogoMapper catalogoMapper;

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
}