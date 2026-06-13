package com.rednorte.ms_citas.service;

import com.rednorte.ms_citas.dto.CitaRequestDTO;
import com.rednorte.ms_citas.dto.CitaResponseDTO;
import com.rednorte.ms_citas.dto.EstadoCita;

import java.util.List;

public interface CitaService {
    CitaResponseDTO crearCita(CitaRequestDTO request);
    CitaResponseDTO obtenerPorId(Long id);
    List<CitaResponseDTO> listarTodas();
    List<CitaResponseDTO> listarPorPaciente(Long pacienteId);
    List<CitaResponseDTO> listarPorEspecialidad(String especialidad);
    List<CitaResponseDTO> listarPorEstado(EstadoCita estado);
    List<CitaResponseDTO> listarPorMedico(String nombreMedico);
    CitaResponseDTO actualizarCita(Long id, CitaRequestDTO request);
    CitaResponseDTO confirmarCita(Long id);
    CitaResponseDTO cancelarCita(Long id);
    void eliminarCita(Long id);
}