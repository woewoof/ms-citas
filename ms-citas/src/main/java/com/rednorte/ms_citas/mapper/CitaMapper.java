package com.rednorte.ms_citas.mapper;
import com.rednorte.ms_citas.dto.CitaRequestDTO;
import com.rednorte.ms_citas.dto.CitaResponseDTO;
import com.rednorte.ms_citas.model.Cita;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class CitaMapper {
    public Cita toEntity(CitaRequestDTO dto){
        return Cita.builder()
                .pacienteId(dto.getPacienteId())
                .fecha(dto.getFecha())
                .hora(dto.getHora())
                .nombreMedico(dto.getNombreMedico())
                .especialidad(dto.getEspecialidad())
                .centroSalud(dto.getCentroSalud())
                .motivo(dto.getMotivo())
                .build();
    }

    public CitaResponseDTO toResponseDTO(Cita cita){
        return CitaResponseDTO.builder()
                .id(cita.getId())
                .pacienteId(cita.getPacienteId())
                .fecha(cita.getFecha())
                .hora(cita.getHora())
                .nombreMedico(cita.getNombreMedico())
                .especialidad(cita.getEspecialidad())
                .centroSalud(cita.getCentroSalud())
                .estado(cita.getEstado())
                .motivo(cita.getMotivo())
                .fechaCreacion(cita.getFechaCreacion())
                .fechaActualizacion(cita.getFechaActualizacion())
                .build();
    }

    public void updateEntityFromDTO(CitaRequestDTO dto, Cita cita){
        cita.setPacienteId(dto.getPacienteId());
        cita.setFecha(dto.getFecha());
        cita.setHora(dto.getHora());
        cita.setNombreMedico(dto.getNombreMedico());
        cita.setEspecialidad(dto.getEspecialidad());
        cita.setCentroSalud(dto.getCentroSalud());
        cita.setMotivo(dto.getMotivo());
    }
}
