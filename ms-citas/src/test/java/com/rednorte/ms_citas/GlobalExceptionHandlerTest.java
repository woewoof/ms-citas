package com.rednorte.ms_citas;

import com.rednorte.ms_citas.exception.ErrorResponseDTO;
import com.rednorte.ms_citas.exception.GlobalExceptionHandler;
import com.rednorte.ms_citas.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleNotFound_debeRetornar404() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/api/v1/citas/1");

        ResponseEntity<ErrorResponseDTO> response = handler.handleNotFound(
                new ResourceNotFoundException("No se encontró la cita"), request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(404, response.getBody().getStatus());
    }
    @Test
    void handleIllegalArgument_debeRetornar400() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/api/v1/citas/1");

        ResponseEntity<ErrorResponseDTO> response = handler.handleIllegalArgument(
                new IllegalArgumentException("La fecha debe ser futura"), request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(400, response.getBody().getStatus());
    }
}