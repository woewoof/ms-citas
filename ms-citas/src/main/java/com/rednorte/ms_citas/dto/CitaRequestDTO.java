package com.rednorte.ms_citas.dto;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.*;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CitaRequestDTO {
    @NotNull(message = "El paciente es obligatorio")
    private Long pacienteId;
}
