package com.clinic.webapi.modules.historiasclinicas.dto;

import com.clinic.webapi.modules.evolucionesmedicas.dto.EvolucionMedicaResponse;
import com.clinic.webapi.modules.pacientes.dto.PacienteResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HistoriaClinicaCompletaResponse {
    private PacienteResponse paciente;
    private HistoriaClinicaResponse historiaClinica;
    private EvolucionMedicaResponse evolucion;
}