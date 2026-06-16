package com.rednorte.ms_citas;

import com.rednorte.ms_citas.dto.CitaRequestDTO;
import com.rednorte.ms_citas.dto.CitaResponseDTO;
import com.rednorte.ms_citas.dto.EstadoCita;
import com.rednorte.ms_citas.exception.ResourceNotFoundException;
import com.rednorte.ms_citas.mapper.CitaMapper;
import com.rednorte.ms_citas.model.Cita;
import com.rednorte.ms_citas.repository.CitaRepository;
import com.rednorte.ms_citas.service.CitaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CitaServiceImplTest {

    @Mock private CitaRepository citaRepository;
    @Mock private CitaMapper citaMapper;
    @InjectMocks private CitaServiceImpl citaService;

    private CitaRequestDTO request;
    private Cita cita;
    private CitaResponseDTO response;

    @BeforeEach
    void setUp() {
        request = CitaRequestDTO.builder()
                .pacienteId(1L)
                .fecha(LocalDate.now().plusDays(1))
                .hora(LocalTime.of(10, 0))
                .nombreMedico("Dr. García")
                .especialidad("Cardiología")
                .build();

        cita = Cita.builder()
                .id(1L)
                .estado(EstadoCita.PROGRAMADA)
                .nombreMedico("Dr. García")
                .fecha(LocalDate.now().plusDays(1))
                .hora(LocalTime.of(10, 0))
                .build();

        response = CitaResponseDTO.builder().id(1L).estado(EstadoCita.PROGRAMADA).build();
    }

    @Test
    void crearCita_exitoso() {
        when(citaRepository.existsByNombreMedicoAndFechaAndHora(any(), any(), any())).thenReturn(false);
        when(citaMapper.toEntity(request)).thenReturn(cita);
        when(citaRepository.save(cita)).thenReturn(cita);
        when(citaMapper.toResponseDTO(cita)).thenReturn(response);

        assertNotNull(citaService.crearCita(request));
    }

    @Test
    void crearCita_fechaPasada_lanzaExcepcion() {
        request.setFecha(LocalDate.now().minusDays(1));
        assertThrows(IllegalArgumentException.class, () -> citaService.crearCita(request));
    }

    @Test
    void crearCita_choqueHorario_lanzaExcepcion() {
        when(citaRepository.existsByNombreMedicoAndFechaAndHora(any(), any(), any())).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> citaService.crearCita(request));
    }

    @Test
    void obtenerPorId_exitoso() {
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));
        when(citaMapper.toResponseDTO(cita)).thenReturn(response);
        assertNotNull(citaService.obtenerPorId(1L));
    }

    @Test
    void obtenerPorId_noExiste_lanzaExcepcion() {
        when(citaRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> citaService.obtenerPorId(99L));
    }

    @Test
    void listarTodas_retornaLista() {
        when(citaRepository.findAll()).thenReturn(List.of(cita));
        when(citaMapper.toResponseDTO(cita)).thenReturn(response);
        assertEquals(1, citaService.listarTodas().size());
    }

    @Test
    void listarPorPaciente_retornaLista() {
        when(citaRepository.findByPacienteId(1L)).thenReturn(List.of(cita));
        when(citaMapper.toResponseDTO(cita)).thenReturn(response);
        assertEquals(1, citaService.listarPorPaciente(1L).size());
    }

    @Test
    void listarPorEspecialidad_retornaLista() {
        when(citaRepository.findByEspecialidad("Cardiología")).thenReturn(List.of(cita));
        when(citaMapper.toResponseDTO(cita)).thenReturn(response);
        assertEquals(1, citaService.listarPorEspecialidad("Cardiología").size());
    }

    @Test
    void listarPorEstado_retornaLista() {
        when(citaRepository.findByEstado(EstadoCita.PROGRAMADA)).thenReturn(List.of(cita));
        when(citaMapper.toResponseDTO(cita)).thenReturn(response);
        assertEquals(1, citaService.listarPorEstado(EstadoCita.PROGRAMADA).size());
    }

    @Test
    void listarPorMedico_retornaLista() {
        when(citaRepository.findByNombreMedico("Dr. García")).thenReturn(List.of(cita));
        when(citaMapper.toResponseDTO(cita)).thenReturn(response);
        assertEquals(1, citaService.listarPorMedico("Dr. García").size());
    }

    @Test
    void confirmarCita_exitoso() {
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));
        when(citaRepository.save(cita)).thenReturn(cita);
        when(citaMapper.toResponseDTO(cita)).thenReturn(response);
        citaService.confirmarCita(1L);
        assertEquals(EstadoCita.CONFIRMADA, cita.getEstado());
    }

    @Test
    void confirmarCita_cancelada_lanzaExcepcion() {
        cita.setEstado(EstadoCita.CANCELADA);
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));
        assertThrows(IllegalArgumentException.class, () -> citaService.confirmarCita(1L));
    }

    @Test
    void cancelarCita_exitoso() {
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));
        when(citaRepository.save(cita)).thenReturn(cita);
        when(citaMapper.toResponseDTO(cita)).thenReturn(response);
        citaService.cancelarCita(1L);
        assertEquals(EstadoCita.CANCELADA, cita.getEstado());
    }

    @Test
    void cancelarCita_completada_lanzaExcepcion() {
        cita.setEstado(EstadoCita.COMPLETADA);
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));
        assertThrows(IllegalArgumentException.class, () -> citaService.cancelarCita(1L));
    }

    @Test
    void marcarNoAsiste_exitoso() {
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));
        when(citaRepository.save(cita)).thenReturn(cita);
        when(citaMapper.toResponseDTO(cita)).thenReturn(response);
        citaService.marcarNoAsiste(1L);
        assertEquals(EstadoCita.NO_ASISTE, cita.getEstado());
    }

    @Test
    void eliminarCita_exitoso() {
        when(citaRepository.existsById(1L)).thenReturn(true);
        citaService.eliminarCita(1L);
        verify(citaRepository).deleteById(1L);
    }

    @Test
    void eliminarCita_noExiste_lanzaExcepcion() {
        when(citaRepository.existsById(99L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> citaService.eliminarCita(99L));
    }
}