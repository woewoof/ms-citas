package com.rednorte.ms_citas;

import com.rednorte.ms_citas.dto.CitaRequestDTO;
import com.rednorte.ms_citas.mapper.CitaMapper;
import com.rednorte.ms_citas.model.Cita;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class CitaMapperTest {

    private final CitaMapper mapper = new CitaMapper();

    @Test
    void toEntity_debeMappearLosCamposCorrectamente() {
        CitaRequestDTO dto = CitaRequestDTO.builder()
                .pacienteId(1L)
                .fecha(LocalDate.now().plusDays(1))
                .hora(LocalTime.of(10, 0))
                .nombreMedico("Dr. García")
                .especialidad("Cardiología")
                .build();

        Cita cita = mapper.toEntity(dto);

        assertEquals("Dr. García", cita.getNombreMedico());
        assertEquals("Cardiología", cita.getEspecialidad());
    }

    @Test
    void updateEntityFromDTO_debeActualizarLosCampos() {
        Cita cita = new Cita();
        CitaRequestDTO dto = CitaRequestDTO.builder()
                .pacienteId(2L)
                .fecha(LocalDate.now().plusDays(2))
                .hora(LocalTime.of(14, 0))
                .nombreMedico("Dr. Nuevo")
                .especialidad("Pediatría")
                .build();

        mapper.updateEntityFromDTO(dto, cita);

        assertEquals("Dr. Nuevo", cita.getNombreMedico());
        assertEquals("Pediatría", cita.getEspecialidad());
    }
}