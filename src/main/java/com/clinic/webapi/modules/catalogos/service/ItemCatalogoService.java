package com.clinic.webapi.modules.catalogos.service;

import com.clinic.webapi.modules.catalogos.dto.ItemCatalogoRequest;
import com.clinic.webapi.modules.catalogos.dto.ItemCatalogoResponse;
import com.clinic.webapi.modules.catalogos.model.entity.Catalogo;
import com.clinic.webapi.modules.catalogos.model.entity.ItemCatalogo;
import com.clinic.webapi.modules.catalogos.model.mapper.ItemCatalogoMapper;
import com.clinic.webapi.modules.catalogos.repository.CatalogoRepository;
import com.clinic.webapi.modules.catalogos.repository.ItemCatalogoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemCatalogoService {

  private final ItemCatalogoRepository itemCatalogoRepository;
  private final CatalogoRepository catalogoRepository;
  private final ItemCatalogoMapper itemCatalogoMapper;

  @Transactional
  public ItemCatalogoResponse crearItemCatalogo(ItemCatalogoRequest request) {
    Catalogo catalogo = catalogoRepository.findById(request.getCatalogoId())
        .orElseThrow(() -> new RuntimeException("Catálogo no encontrado con ID: " + request.getCatalogoId()));

    if (itemCatalogoRepository.existsByCatalogoAndNombre(catalogo, request.getNombre())) {
      throw new RuntimeException("Ya existe un ítem con el nombre: " + request.getNombre() + " en este catálogo");
    }

    ItemCatalogo itemCatalogo = itemCatalogoMapper.toEntity(request, catalogo);
    ItemCatalogo itemGuardado = itemCatalogoRepository.save(itemCatalogo);

    return itemCatalogoMapper.toResponse(itemGuardado);
  }

  @Transactional(readOnly = true)
  public List<ItemCatalogoResponse> obtenerItemsPorCatalogo(UUID catalogoId) {
    Catalogo catalogo = catalogoRepository.findById(catalogoId)
        .orElseThrow(() -> new RuntimeException("Catálogo no encontrado con ID: " + catalogoId));

    return itemCatalogoRepository.findByCatalogo(catalogo)
        .stream()
        .map(itemCatalogoMapper::toResponse)
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<ItemCatalogoResponse> obtenerItemsPorTipo(String tipo) {
    return itemCatalogoRepository.findByTipoCatalogo(tipo)
        .stream()
        .map(itemCatalogoMapper::toResponse)
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<ItemCatalogoResponse> obtenerItemsPorNombreCatalogo(String catalogoNombre) {
    return itemCatalogoRepository.findByNombreCatalogo(catalogoNombre)
        .stream()
        .map(itemCatalogoMapper::toResponse)
        .collect(Collectors.toList());
  }

  @Transactional
  public ItemCatalogoResponse actualizarItemCatalogo(UUID id, ItemCatalogoRequest request) {
    ItemCatalogo itemExistente = itemCatalogoRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Ítem de catálogo no encontrado con ID: " + id));

    Catalogo catalogo = catalogoRepository.findById(request.getCatalogoId())
        .orElseThrow(() -> new RuntimeException("Catálogo no encontrado con ID: " + request.getCatalogoId()));

    if (!request.getNombre().equals(itemExistente.getNombre()) &&
        itemCatalogoRepository.existsByCatalogoAndNombre(catalogo, request.getNombre())) {
      throw new RuntimeException("Ya existe un ítem con el nombre: " + request.getNombre() + " en este catálogo");
    }

    ItemCatalogo itemActualizado = itemCatalogoMapper.updateEntityFromRequest(itemExistente, request, catalogo);
    ItemCatalogo itemGuardado = itemCatalogoRepository.save(itemActualizado);

    return itemCatalogoMapper.toResponse(itemGuardado);
  }

  @Transactional
  public void eliminarItemCatalogo(UUID id) {
    ItemCatalogo itemCatalogo = itemCatalogoRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Ítem de catálogo no encontrado con ID: " + id));
    itemCatalogoRepository.delete(itemCatalogo);
  }

  @Transactional
  public void desactivarItemCatalogo(UUID id) {
    ItemCatalogo itemCatalogo = itemCatalogoRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Ítem de catálogo no encontrado con ID: " + id));
    itemCatalogo.setEstaActivo(false);
    itemCatalogoRepository.save(itemCatalogo);
  }
}