package com.rednorte.ms_citas;

import com.rednorte.ms_citas.dto.EstadoCita;
import com.rednorte.ms_citas.model.Cita;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CitaModelTest {

    @Test
    void prePersist_debeSetearEstadoProgramada() {
        Cita cita = new Cita();
        cita.prePersist();

        assertEquals(EstadoCita.PROGRAMADA, cita.getEstado());
        assertNotNull(cita.getFechaCreacion());
    }
}