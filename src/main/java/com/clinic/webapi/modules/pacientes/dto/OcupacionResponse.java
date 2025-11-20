package com.clinic.webapi.modules.pacientes.dto;

import com.clinic.webapi.modules.catalogos.dto.ItemCatalogoResponse;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class OcupacionResponse {
    private UUID id;
    private ItemCatalogoResponse ocupacion;
    private String nombreEmpresa;
    private String cargo;
    private String telefonoEmpresa;
    private String direccionEmpresa;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Boolean actual;
    private Instant fechaCreacion;
    private Instant fechaActualizacion;
}
