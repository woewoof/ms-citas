package com.rednorte.ms_citas.repository;

import com.rednorte.ms_citas.dto.EstadoCita;
import com.rednorte.ms_citas.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {
    List<Cita> findByPacienteId(Long pacienteId);
    List<Cita> findByEspecialidad(String especialidad);
    List<Cita> findByEstado(EstadoCita estado);
    List<Cita> findByNombreMedico(String nombreMedico);

    boolean existsByNombreMedicoAndFechaAndHora(String nombreMedico, LocalDate fecha, LocalTime hora);
}