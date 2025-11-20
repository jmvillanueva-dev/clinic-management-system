package com.clinic.webapi.modules.pacientes.dto;

import com.clinic.webapi.modules.catalogos.dto.ItemCatalogoResponse;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class FuenteInformacionResponse {
    private UUID id;
    private ItemCatalogoResponse fuenteInformacion;
    private String nombreFuenteInfo;
    private String telefono;
    private String observaciones;
    private Instant fechaCreacion;
    private Instant fechaActualizacion;
}
