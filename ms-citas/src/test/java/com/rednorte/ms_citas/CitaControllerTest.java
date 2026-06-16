package com.rednorte.ms_citas;

import com.rednorte.ms_citas.controller.CitaController;
import com.rednorte.ms_citas.dto.CitaResponseDTO;
import com.rednorte.ms_citas.dto.EstadoCita;
import com.rednorte.ms_citas.service.CitaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CitaController.class)
class CitaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CitaService citaService;

    @Test
    void listarTodas_debeRetornar200() throws Exception {
        when(citaService.listarTodas()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/citas"))
                .andExpect(status().isOk());
    }

    @Test
    void obtenerPorId_debeRetornar200() throws Exception {
        when(citaService.obtenerPorId(1L)).thenReturn(
                CitaResponseDTO.builder().id(1L).estado(EstadoCita.PROGRAMADA).build());

        mockMvc.perform(get("/api/v1/citas/1"))
                .andExpect(status().isOk());
    }
}
