package com.clinic.webapi.modules.evolucionesmedicas.service;

import com.clinic.webapi.modules.evolucionesmedicas.dto.EvolucionSignosVitalesRequest;
import com.clinic.webapi.modules.evolucionesmedicas.dto.EvolucionSignosVitalesResponse;
import com.clinic.webapi.modules.evolucionesmedicas.entity.EvolucionMedica;
import com.clinic.webapi.modules.evolucionesmedicas.entity.EvolucionSignosVitales;
import com.clinic.webapi.modules.evolucionesmedicas.repository.EvolucionMedicaRepository;
import com.clinic.webapi.modules.evolucionesmedicas.repository.EvolucionSignosVitalesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EvolucionSignosVitalesService {

  private final EvolucionSignosVitalesRepository signosVitalesRepository;
  private final EvolucionMedicaRepository evolucionMedicaRepository;

  @Transactional
  public EvolucionSignosVitalesResponse crearSignosVitales(UUID evolucionId, EvolucionSignosVitalesRequest request) {
    EvolucionMedica evolucionMedica = evolucionMedicaRepository.findById(evolucionId)
        .orElseThrow(() -> new RuntimeException("Evolución médica no encontrada con ID: " + evolucionId));

    // Verificar si ya existen signos vitales para esta evolución
    if (signosVitalesRepository.existsByEvolucionMedicaId(evolucionId)) {
      throw new RuntimeException("Ya existen signos vitales registrados para esta evolución médica");
    }

    // Calcular IMC automáticamente
    BigDecimal imc = calcularIMC(request.getPeso(), request.getTalla());

    EvolucionSignosVitales signosVitales = EvolucionSignosVitales.builder()
        .evolucionMedica(evolucionMedica)
        .presionArterialSistolica(request.getPresionArterialSistolica())
        .presionArterialDiastolica(request.getPresionArterialDiastolica())
        .frecuenciaCardiaca(request.getFrecuenciaCardiaca())
        .frecuenciaRespiratoria(request.getFrecuenciaRespiratoria())
        .temperatura(request.getTemperatura())
        .saturacionOxigeno(request.getSaturacionOxigeno())
        .peso(request.getPeso())
        .talla(request.getTalla())
        .imc(imc)
        .glucosa(request.getGlucosa())
        .build();

    EvolucionSignosVitales signosGuardados = signosVitalesRepository.save(signosVitales);

    return construirResponse(signosGuardados);
  }

  @Transactional(readOnly = true)
  public EvolucionSignosVitalesResponse obtenerSignosVitales(UUID evolucionId) {
    EvolucionSignosVitales signosVitales = signosVitalesRepository.findByEvolucionMedicaId(evolucionId)
        .orElseThrow(() -> new RuntimeException("Signos vitales no encontrados para la evolución médica con ID: " + evolucionId));

    return construirResponse(signosVitales);
  }

  @Transactional
  public EvolucionSignosVitalesResponse actualizarSignosVitales(UUID evolucionId, EvolucionSignosVitalesRequest request) {
    EvolucionSignosVitales signosExistente = signosVitalesRepository.findByEvolucionMedicaId(evolucionId)
        .orElseThrow(() -> new RuntimeException("Signos vitales no encontrados para la evolución médica con ID: " + evolucionId));

    // Calcular IMC automáticamente
    BigDecimal imc = calcularIMC(request.getPeso(), request.getTalla());

    signosExistente.setPresionArterialSistolica(request.getPresionArterialSistolica());
    signosExistente.setPresionArterialDiastolica(request.getPresionArterialDiastolica());
    signosExistente.setFrecuenciaCardiaca(request.getFrecuenciaCardiaca());
    signosExistente.setFrecuenciaRespiratoria(request.getFrecuenciaRespiratoria());
    signosExistente.setTemperatura(request.getTemperatura());
    signosExistente.setSaturacionOxigeno(request.getSaturacionOxigeno());
    signosExistente.setPeso(request.getPeso());
    signosExistente.setTalla(request.getTalla());
    signosExistente.setImc(imc);
    signosExistente.setGlucosa(request.getGlucosa());

    EvolucionSignosVitales signosActualizados = signosVitalesRepository.save(signosExistente);

    return construirResponse(signosActualizados);
  }

  @Transactional
  public void eliminarSignosVitales(UUID evolucionId) {
    EvolucionSignosVitales signosVitales = signosVitalesRepository.findByEvolucionMedicaId(evolucionId)
        .orElseThrow(() -> new RuntimeException("Signos vitales no encontrados para la evolución médica con ID: " + evolucionId));

    signosVitalesRepository.delete(signosVitales);
  }

  private BigDecimal calcularIMC(BigDecimal peso, BigDecimal talla) {
    if (peso == null || talla == null || talla.compareTo(BigDecimal.ZERO) <= 0) {
      return null;
    }

    // IMC = peso (kg) / (talla (m) * talla (m))
    BigDecimal tallaMetros = talla.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    return peso.divide(tallaMetros.pow(2), 2, RoundingMode.HALF_UP);
  }

  private EvolucionSignosVitalesResponse construirResponse(EvolucionSignosVitales signosVitales) {
    return EvolucionSignosVitalesResponse.builder()
        .id(signosVitales.getId())
        .evolucionMedicaId(signosVitales.getEvolucionMedica().getId())
        .presionArterialSistolica(signosVitales.getPresionArterialSistolica())
        .presionArterialDiastolica(signosVitales.getPresionArterialDiastolica())
        .frecuenciaCardiaca(signosVitales.getFrecuenciaCardiaca())
        .frecuenciaRespiratoria(signosVitales.getFrecuenciaRespiratoria())
        .temperatura(signosVitales.getTemperatura())
        .saturacionOxigeno(signosVitales.getSaturacionOxigeno())
        .peso(signosVitales.getPeso())
        .talla(signosVitales.getTalla())
        .imc(signosVitales.getImc())
        .glucosa(signosVitales.getGlucosa())
        .fechaCreacion(signosVitales.getFechaCreacion())
        .build();
  }
}