package com.clinic.webapi.modules.pacientes.service;

import com.clinic.webapi.modules.catalogos.repository.ItemCatalogoRepository;
import com.clinic.webapi.modules.pacientes.dto.PacienteRequest;
import com.clinic.webapi.modules.pacientes.dto.PacienteResponse;
import com.clinic.webapi.modules.pacientes.model.entity.*;
import com.clinic.webapi.modules.pacientes.model.mapper.PacienteMapper;
import com.clinic.webapi.modules.pacientes.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository pacienteRepository;
    private final PacienteDatosDemograficosRepository datosDemograficosRepository;
    private final PacienteUbicacionGeograficaRepository ubicacionGeograficaRepository;
    private final PacienteOcupacionRepository ocupacionRepository;
    private final PacienteFuenteInformacionRepository fuenteInformacionRepository;
    private final PacienteContactoEmergenciaRepository contactoEmergenciaRepository;
    private final PacienteAntecedenteClinicoRepository antecedenteClinicoRepository;
    private final ItemCatalogoRepository itemCatalogoRepository;
    private final PacienteMapper pacienteMapper;

    @Transactional
    public PacienteResponse crearPaciente(PacienteRequest request) {
        // 1. Validaciones
        if (pacienteRepository.existsByCedula(request.getCedula())) {
            throw new RuntimeException("Ya existe un paciente con la cédula: " + request.getCedula());
        }

        // 2. Crear y guardar el paciente primero
        Paciente paciente = crearPacienteBasico(request);
        Paciente pacienteGuardado = pacienteRepository.save(paciente);

        // 3. Crear y guardar las entidades relacionadas Y ASIGNARLAS EN MEMORIA
        var datosDemograficos = crearDatosDemograficos(pacienteGuardado, request);
        pacienteGuardado.setDatosDemograficos(datosDemograficos);

        var ubicacion = crearUbicacionGeografica(pacienteGuardado, request);
        pacienteGuardado.setUbicacionGeografica(ubicacion);

        var ocupacion = crearOcupacion(pacienteGuardado, request);
        pacienteGuardado.setOcupacion(ocupacion);

        var fuenteInfo = crearFuenteInformacion(pacienteGuardado, request);
        pacienteGuardado.setFuenteInformacion(fuenteInfo);

        List<PacienteContactoEmergencia> contactos = crearContactosEmergencia(pacienteGuardado, request);
        pacienteGuardado.setContactosEmergencia(contactos);

        List<PacienteAntecedenteClinico> antecedentes = crearAntecedentesClinicos(pacienteGuardado, request);
        pacienteGuardado.setAntecedentesClinicos(antecedentes);

        // 4. Devolver respuesta
        return pacienteMapper.toResponse(pacienteGuardado);
    }

    private Paciente crearPacienteBasico(PacienteRequest request) {
        Paciente paciente = Paciente.builder()
                .cedula(request.getCedula())
                .primerNombre(request.getPrimerNombre())
                .segundoNombre(request.getSegundoNombre())
                .apellidoPaterno(request.getApellidoPaterno())
                .apellidoMaterno(request.getApellidoMaterno())
                .email(request.getEmail())
                .telefono(request.getTelefono())
                .estaActivo(true)
                .build();

        // Asignar grupo sanguíneo si existe
        if (request.getGrupoSanguineoId() != null) {
            var grupoSanguineo = itemCatalogoRepository.findById(request.getGrupoSanguineoId())
                    .orElseThrow(() -> new RuntimeException("Grupo sanguíneo no encontrado"));
            paciente.setGrupoSanguineo(grupoSanguineo);
        }

        return paciente;
    }

    private PacienteDatosDemograficos crearDatosDemograficos(Paciente paciente, PacienteRequest request) {
        var genero = itemCatalogoRepository.findById(request.getGeneroId())
                .orElseThrow(() -> new RuntimeException("Género no encontrado"));

        PacienteDatosDemograficos datosDemograficos = PacienteDatosDemograficos.builder()
                .paciente(paciente)
                .fechaNacimiento(request.getFechaNacimiento())
                .lugarNacimiento(request.getLugarNacimiento())
                .genero(genero)
                .nacionalidad(request.getNacionalidad())
                .build();

        // Asignar campos opcionales
        if (request.getGrupoCulturalId() != null) {
            var grupoCultural = itemCatalogoRepository.findById(request.getGrupoCulturalId())
                    .orElseThrow(() -> new RuntimeException("Grupo cultural no encontrado"));
            datosDemograficos.setGrupoCultural(grupoCultural);
        }

        if (request.getEstadoCivilId() != null) {
            var estadoCivil = itemCatalogoRepository.findById(request.getEstadoCivilId())
                    .orElseThrow(() -> new RuntimeException("Estado civil no encontrado"));
            datosDemograficos.setEstadoCivil(estadoCivil);
        }

        if (request.getNivelInstruccionId() != null) {
            var nivelInstruccion = itemCatalogoRepository.findById(request.getNivelInstruccionId())
                    .orElseThrow(() -> new RuntimeException("Nivel de instrucción no encontrado"));
            datosDemograficos.setNivelInstruccion(nivelInstruccion);
        }

        return datosDemograficosRepository.save(datosDemograficos);
    }

    private PacienteUbicacionGeografica crearUbicacionGeografica(Paciente paciente, PacienteRequest request) {
        PacienteUbicacionGeografica ubicacion = PacienteUbicacionGeografica.builder()
                .paciente(paciente)
                .direccion(request.getDireccion())
                .canton(request.getCanton())
                .parroquia(request.getParroquia())
                .build();

        if (request.getProvinciaId() != null) {
            var provincia = itemCatalogoRepository.findById(request.getProvinciaId())
                    .orElseThrow(() -> new RuntimeException("Provincia no encontrada"));
            ubicacion.setProvincia(provincia);
        }

        return ubicacionGeograficaRepository.save(ubicacion);
    }

    private PacienteOcupacion crearOcupacion(Paciente paciente, PacienteRequest request) {
        if (request.getOcupacionId() != null) {
            var ocupacionCatalogo = itemCatalogoRepository.findById(request.getOcupacionId())
                    .orElseThrow(() -> new RuntimeException("Ocupación no encontrada"));

            PacienteOcupacion ocupacion = PacienteOcupacion.builder()
                    .paciente(paciente)
                    .ocupacion(ocupacionCatalogo)
                    .nombreEmpresa(request.getNombreEmpresa())
                    .cargo(request.getCargo())
                    .telefonoEmpresa(request.getTelefonoEmpresa())
                    .direccionEmpresa(request.getDireccionEmpresa())
                    .fechaInicio(request.getFechaInicio())
                    .fechaFin(request.getFechaFin())
                    .actual(request.getActual() != null ? request.getActual() : true)
                    .build();

            return ocupacionRepository.save(ocupacion);
        }
        return null;
    }

    private PacienteFuenteInformacion crearFuenteInformacion(Paciente paciente, PacienteRequest request) {
        if (request.getFuenteInformacionId() != null) {
            var fuenteInfoCatalogo = itemCatalogoRepository.findById(request.getFuenteInformacionId())
                    .orElseThrow(() -> new RuntimeException("Fuente de información no encontrada"));

            PacienteFuenteInformacion fuenteInformacion = PacienteFuenteInformacion.builder()
                    .paciente(paciente)
                    .fuenteInformacion(fuenteInfoCatalogo)
                    .nombreFuenteInfo(request.getNombreFuenteInfo())
                    .telefono(request.getTelefonoFuenteInfo())
                    .observaciones(request.getObservacionesFuente())
                    .build();

            return fuenteInformacionRepository.save(fuenteInformacion);
        }
        return null;
    }

    private List<PacienteContactoEmergencia> crearContactosEmergencia(Paciente paciente, PacienteRequest request) {
        if (request.getContactosEmergencia() != null && !request.getContactosEmergencia().isEmpty()) {
            List<PacienteContactoEmergencia> contactos = new ArrayList<>();

            for (var contactoRequest : request.getContactosEmergencia()) {
                var parentesco = itemCatalogoRepository.findById(contactoRequest.getParentescoId())
                        .orElseThrow(() -> new RuntimeException("Parentesco no encontrado"));

                PacienteContactoEmergencia contacto = PacienteContactoEmergencia.builder()
                        .paciente(paciente)
                        .nombre(contactoRequest.getNombre())
                        .parentesco(parentesco)
                        .telefono(contactoRequest.getTelefono())
                        .direccion(contactoRequest.getDireccion())
                        .build();

                contactos.add(contacto);
            }

            return contactoEmergenciaRepository.saveAll(contactos);
        }
        return new ArrayList<>();
    }

    private List<PacienteAntecedenteClinico> crearAntecedentesClinicos(Paciente paciente, PacienteRequest request) {
        if (request.getAntecedentesClinicos() != null && !request.getAntecedentesClinicos().isEmpty()) {
            List<PacienteAntecedenteClinico> antecedentes = new ArrayList<>();

            for (var antecedenteRequest : request.getAntecedentesClinicos()) {
                var tipoAntecedente = itemCatalogoRepository.findById(antecedenteRequest.getTipoAntecedenteId())
                        .orElseThrow(() -> new RuntimeException("Tipo de antecedente no encontrado"));

                var patologia = itemCatalogoRepository.findById(antecedenteRequest.getPatologiaId())
                        .orElseThrow(() -> new RuntimeException("Patología no encontrada"));

                PacienteAntecedenteClinico antecedente = PacienteAntecedenteClinico.builder()
                        .paciente(paciente)
                        .tipoAntecedente(tipoAntecedente)
                        .patologia(patologia)
                        .descripcion(antecedenteRequest.getDescripcion())
                        .fechaDiagnostico(antecedenteRequest.getFechaDiagnostico())
                        .tratamiento(antecedenteRequest.getTratamiento())
                        .estaActivo(antecedenteRequest.getEstaActivo() != null ? antecedenteRequest.getEstaActivo() : true)
                        .build();

                antecedentes.add(antecedente);
            }

            return antecedenteClinicoRepository.saveAll(antecedentes);
        }
        return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public List<PacienteResponse> obtenerTodosLosPacientes() {
        return pacienteRepository.findAllByEstaActivo(true)
                .stream()
                .map(pacienteMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PacienteResponse obtenerPacientePorId(UUID id) {
        Paciente paciente = pacienteRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con ID: " + id));
        return pacienteMapper.toResponse(paciente);
    }

    @Transactional(readOnly = true)
    public PacienteResponse obtenerPacientePorCedula(String cedula) {
        Paciente paciente = pacienteRepository.findByCedula(cedula)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con cédula: " + cedula));
        return pacienteMapper.toResponse(paciente);
    }

    @Transactional
    public PacienteResponse actualizarPaciente(UUID id, PacienteRequest request) {
        Paciente pacienteExistente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con ID: " + id));

        // Validar cédula si cambió
        if (!request.getCedula().equals(pacienteExistente.getCedula()) &&
                pacienteRepository.existsByCedula(request.getCedula())) {
            throw new RuntimeException("Ya existe un paciente con la cédula: " + request.getCedula());
        }

        Paciente pacienteActualizado = pacienteMapper.updateEntityFromRequest(pacienteExistente, request);
        Paciente pacienteGuardado = pacienteRepository.save(pacienteActualizado);

        return pacienteMapper.toResponse(pacienteGuardado);
    }

    @Transactional
    public void eliminarPaciente(UUID id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con ID: " + id));

        paciente.setEstaActivo(false);
        pacienteRepository.save(paciente);
    }

    @Transactional(readOnly = true)
    public List<PacienteResponse> buscarPacientes(String searchTerm) {
        return pacienteRepository.searchActivePacientes(searchTerm)
                .stream()
                .map(pacienteMapper::toResponse)
                .collect(Collectors.toList());
    }
}
