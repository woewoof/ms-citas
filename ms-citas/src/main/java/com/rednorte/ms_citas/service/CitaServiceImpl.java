package com.rednorte.ms_citas.service;

import com.rednorte.ms_citas.dto.CitaRequestDTO;
import com.rednorte.ms_citas.dto.CitaResponseDTO;
import com.rednorte.ms_citas.dto.EstadoCita;
import com.rednorte.ms_citas.exception.ResourceNotFoundException;
import com.rednorte.ms_citas.mapper.CitaMapper;
import com.rednorte.ms_citas.model.Cita;
import com.rednorte.ms_citas.repository.CitaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CitaServiceImpl implements CitaService {

    private final CitaRepository citaRepository;
    private final CitaMapper citaMapper;

    @Override
    @Transactional
    public CitaResponseDTO crearCita(CitaRequestDTO request) {
        if (!request.getFecha().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de la cita debe ser futura");
        }
        if (citaRepository.existsByNombreMedicoAndFechaAndHora(
                request.getNombreMedico(), request.getFecha(), request.getHora())) {
            throw new IllegalArgumentException("Ese horario ya esta ocupado para este medico");
        }

        Cita cita = citaMapper.toEntity(request);
        if (cita.getPacienteId() == null) {
            cita.setEstado(EstadoCita.DISPONIBLE);
        } else {
            cita.setEstado(EstadoCita.PROGRAMADA);
        }

        return citaMapper.toResponseDTO(citaRepository.save(cita));
    }

    @Override
    public CitaResponseDTO obtenerPorId(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada"));
        return citaMapper.toResponseDTO(cita);
    }

    @Override
    public List<CitaResponseDTO> listarTodas() {
        return citaRepository.findAll().stream()
                .map(citaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CitaResponseDTO> listarPorPaciente(Long pacienteId) {
        return citaRepository.findByPacienteId(pacienteId).stream()
                .map(citaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CitaResponseDTO> listarPorEspecialidad(String especialidad) {
        return citaRepository.findByEspecialidad(especialidad).stream()
                .map(citaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CitaResponseDTO> listarPorEstado(EstadoCita estado) {
        return citaRepository.findByEstado(estado).stream()
                .map(citaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CitaResponseDTO> listarPorMedico(String nombreMedico) {
        return citaRepository.findByNombreMedico(nombreMedico).stream()
                .map(citaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CitaResponseDTO actualizarCita(Long id, CitaRequestDTO request) {
        if (!request.getFecha().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de la cita debe ser futura");
        }

        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("La cita que quieres actualizar no existe"));

        boolean mismoMedico = cita.getNombreMedico().equals(request.getNombreMedico());
        boolean mismaFecha = cita.getFecha().equals(request.getFecha());
        boolean mismaHora = cita.getHora().equals(request.getHora());

        if (!(mismoMedico && mismaFecha && mismaHora)) {
            if (citaRepository.existsByNombreMedicoAndFechaAndHora(
                    request.getNombreMedico(), request.getFecha(), request.getHora())) {
                throw new IllegalArgumentException("Ese horario ya esta ocupado para este medico");
            }
        }

        citaMapper.updateEntityFromDTO(request, cita);
        if (cita.getPacienteId() == null) {
            cita.setEstado(EstadoCita.DISPONIBLE);
        } else if (cita.getEstado() == EstadoCita.DISPONIBLE) {
            cita.setEstado(EstadoCita.PROGRAMADA);
        }

        return citaMapper.toResponseDTO(citaRepository.save(cita));
    }

    @Override
    @Transactional
    public CitaResponseDTO confirmarCita(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se puede confirmar una cita que no existe"));

        cita.setEstado(EstadoCita.CONFIRMADA);
        return citaMapper.toResponseDTO(citaRepository.save(cita));
    }

    @Override
    @Transactional
    public CitaResponseDTO cancelarCita(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se puede cancelar una cita que no existe"));

        cita.setPacienteId(null);
        cita.setEstado(EstadoCita.DISPONIBLE);
        return citaMapper.toResponseDTO(citaRepository.save(cita));
    }

    @Override
    @Transactional
    public void eliminarCita(Long id) {
        if (!citaRepository.existsById(id)) {
            throw new ResourceNotFoundException("No se puede eliminar una cita que no existe");
        }
        citaRepository.deleteById(id);
    }
}