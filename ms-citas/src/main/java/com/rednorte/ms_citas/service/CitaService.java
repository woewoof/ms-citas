package com.rednorte.ms_citas.service;
import com.rednorte.ms_citas.dto.CitaRequestDTO;
import com.rednorte.ms_citas.dto.CitaResponseDTO;
import com.rednorte.ms_citas.dto.EstadoCita;

import java.util.List;

public interface CitaService {
    CitaResponseDTO crearCita(CitaRequestDTO request);
    CitaResponseDTO obtenerPorId(Long Id);
    List<CitaResponseDTO> listarTodas();
    List<CitaResponseDTO> listarPorPaciente(Long pacienteId);
    List<CitaResponseDTO> listarPorEspecialidad(String especialidad);
    List<CitaResponseDTO> listarPorEstado(EstadoCita estado);
    List<CitaResponseDTO> listarPorMedico(String nombreMedico);
    CitaResponseDTO actualizarCita (Long Id, CitaRequestDTO request);
    CitaRequestDTO  confirmarCita (Long Id);
    CitaRequestDTO cancelarCita (Long Id);
    void eliminarCita (Long Id);
}