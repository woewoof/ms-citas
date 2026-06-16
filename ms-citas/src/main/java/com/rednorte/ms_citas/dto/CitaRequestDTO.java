package com.rednorte.ms_citas.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CitaRequestDTO {
    private Long pacienteId;

    @NotNull
    @Future(message = "La fecha debe ser futura")
    private LocalDate fecha;

    @NotNull
    private LocalTime hora;

    @NotBlank
    private String nombreMedico;

    @NotBlank
    private String especialidad;

    private String centroSalud;
    private String motivo;
}