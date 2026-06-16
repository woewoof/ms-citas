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
        boolean horarioOcupado = citaRepository.existsByNombreMedicoAndFechaAndHora(
                request.getNombreMedico(), request.getFecha(), request.getHora());

        if (horarioOcupado) {
            throw new IllegalArgumentException("Ese horario ya esta ocupado ");
        }
        Cita cita = citaMapper.toEntity(request);
        cita.setEstado(EstadoCita.PROGRAMADA);

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

        boolean cambioMedico = !cita.getNombreMedico().equals(request.getNombreMedico());
        boolean cambioFecha  = !cita.getFecha().equals(request.getFecha());
        boolean cambioHora   = !cita.getHora().equals(request.getHora());

        if (cambioMedico || cambioFecha || cambioHora) {
            boolean horarioOcupado = citaRepository.existsByNombreMedicoAndFechaAndHora(
                    request.getNombreMedico(), request.getFecha(), request.getHora());
            if (horarioOcupado) {
                throw new IllegalArgumentException("Ese horario ya esta ocupado");
            }
        }

        citaMapper.updateEntityFromDTO(request, cita);
        return citaMapper.toResponseDTO(citaRepository.save(cita));
    }
    @Override
    @Transactional
    public CitaResponseDTO confirmarCita(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la cita que deseas confirmar"));

        if (cita.getEstado() == EstadoCita.CANCELADA || cita.getEstado() == EstadoCita.COMPLETADA) {
            throw new IllegalArgumentException("No se puede confirmar una cita cancelada o completada");
        }

        cita.setEstado(EstadoCita.CONFIRMADA);
        return citaMapper.toResponseDTO(citaRepository.save(cita));
    }

    @Override
    @Transactional
    public CitaResponseDTO cancelarCita(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se puede cancelar una cita que no existe"));

        if (cita.getEstado() == EstadoCita.COMPLETADA) {
            throw new IllegalArgumentException("No se puede cancelar una cita completada");
        }

        cita.setEstado(EstadoCita.CANCELADA);
        return citaMapper.toResponseDTO(citaRepository.save(cita));
    }

    @Override
    @Transactional
    public CitaResponseDTO marcarNoAsiste(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la cita que deseas marcar " ));

        if (cita.getEstado() == EstadoCita.CANCELADA || cita.getEstado() == EstadoCita.COMPLETADA) {
            throw new IllegalArgumentException("No se puede marcar como no asiste una cita cancelada o completada");
        }

        cita.setEstado(EstadoCita.NO_ASISTE);
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