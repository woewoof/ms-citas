package com.rednorte.ms_citas.controller;

import com.rednorte.ms_citas.dto.CitaRequestDTO;
import com.rednorte.ms_citas.dto.CitaResponseDTO;
import com.rednorte.ms_citas.dto.EstadoCita;
import com.rednorte.ms_citas.service.CitaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/citas")
@RequiredArgsConstructor
@Tag(name = "Citas", description = "Gestión de citas médicas")
public class CitaController {

    private final CitaService citaService;

    @Operation(summary = "Crear una nueva cita")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cita creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o conflicto de horario")
    })
    @PostMapping
    public ResponseEntity<CitaResponseDTO> crearCita(@Valid @RequestBody CitaRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(citaService.crearCita(request));
    }

    @Operation(summary = "Listar todas las citas")
    @ApiResponse(responseCode = "200", description = "Lista de citas")
    @GetMapping
    public ResponseEntity<List<CitaResponseDTO>> listarTodas() {
        return ResponseEntity.ok(citaService.listarTodas());
    }

    @Operation(summary = "Obtener cita por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cita encontrada"),
            @ApiResponse(responseCode = "404", description = "Cita no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CitaResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(citaService.obtenerPorId(id));
    }

    @Operation(summary = "Listar citas por paciente")
    @ApiResponse(responseCode = "200", description = "Citas del paciente")
    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<CitaResponseDTO>> listarPorPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(citaService.listarPorPaciente(pacienteId));
    }

    @Operation(summary = "Listar citas por especialidad")
    @ApiResponse(responseCode = "200", description = "Citas por especialidad")
    @GetMapping("/especialidad/{especialidad}")
    public ResponseEntity<List<CitaResponseDTO>> listarPorEspecialidad(@PathVariable String especialidad) {
        return ResponseEntity.ok(citaService.listarPorEspecialidad(especialidad));
    }

    @Operation(summary = "Listar citas por estado")
    @ApiResponse(responseCode = "200", description = "Citas por estado")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<CitaResponseDTO>> listarPorEstado(@PathVariable EstadoCita estado) {
        return ResponseEntity.ok(citaService.listarPorEstado(estado));
    }

    @Operation(summary = "Listar citas por médico")
    @ApiResponse(responseCode = "200", description = "Citas del médico")
    @GetMapping("/medico")
    public ResponseEntity<List<CitaResponseDTO>> listarPorMedico(
            @RequestParam String nombreMedico) {
        return ResponseEntity.ok(citaService.listarPorMedico(nombreMedico));
    }

    @Operation(summary = "Actualizar cita")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cita actualizada"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Cita no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CitaResponseDTO> actualizarCita(
            @PathVariable Long id,
            @Valid @RequestBody CitaRequestDTO request) {
        return ResponseEntity.ok(citaService.actualizarCita(id, request));
    }

    @Operation(summary = "Confirmar cita")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cita confirmada"),
            @ApiResponse(responseCode = "400", description = "Estado inválido para confirmar"),
            @ApiResponse(responseCode = "404", description = "Cita no encontrada")
    })
    @PatchMapping("/{id}/confirmar")
    public ResponseEntity<CitaResponseDTO> confirmarCita(@PathVariable Long id) {
        return ResponseEntity.ok(citaService.confirmarCita(id));
    }

    @Operation(summary = "Cancelar cita")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cita cancelada"),
            @ApiResponse(responseCode = "400", description = "Estado inválido para cancelar"),
            @ApiResponse(responseCode = "404", description = "Cita no encontrada")
    })
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<CitaResponseDTO> cancelarCita(@PathVariable Long id) {
        return ResponseEntity.ok(citaService.cancelarCita(id));
    }

    @Operation(summary = "Eliminar cita")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cita eliminada"),
            @ApiResponse(responseCode = "404", description = "Cita no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCita(@PathVariable Long id) {
        citaService.eliminarCita(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Marcar que el paciente no asistió")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cita marcada como no asiste"),
            @ApiResponse(responseCode = "400", description = "Estado inválido"),
            @ApiResponse(responseCode = "404", description = "Cita no encontrada")
    })
    @PatchMapping("/{id}/no-asiste")
    public ResponseEntity<CitaResponseDTO> marcarNoAsiste(@PathVariable Long id) {
        return ResponseEntity.ok(citaService.marcarNoAsiste(id));
    }
}
