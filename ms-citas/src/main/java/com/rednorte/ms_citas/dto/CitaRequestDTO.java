package com.rednorte.ms_citas.dto;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Future;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CitaRequestDTO {
    @NotNull
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
