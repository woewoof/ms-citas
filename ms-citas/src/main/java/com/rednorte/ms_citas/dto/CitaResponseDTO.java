package com.rednorte.ms_citas.dto;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class CitaResponseDTO {
    private Long id;
    private Long pacienteId;
    private LocalDate fecha;
    private LocalTime hora;
    private String nombreMedico;
    private String especialidad;
    private String centroSalud;
    private EstadoCita estado;
    private String motivo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

}
