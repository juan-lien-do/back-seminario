package com.example.demo.Envios.Envios.dto;

import com.example.demo.Envios.CambiosEstadoEnvio.dto.CambioEstadoDTO;
import com.example.demo.Envios.DetallesEnvioComputadora.dto.DetalleEnvioComputadoraDTO;
import com.example.demo.Envios.DetallesEnvioComputadora.dto.DetalleEnvioComputadoraResponseDTO;
import com.example.demo.Envios.DetallesEnvioRecurso.dto.DetalleEnvioRecursoDTO;
import com.example.demo.Envios.DetallesEnvioRecurso.dto.DetalleEnvioRecursoResponseDTO;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EnvioResponseDTO {
    Long idEnvio;
    Long cuilEmpleado;
    String nombreUsuario;
    String nombreEmpleado;
    LocalDate fechaPreparacion;
    List<DetalleEnvioComputadoraResponseDTO> detallesEnvioComputadora;
    List<DetalleEnvioRecursoResponseDTO> detallesEnvioRecurso;
    List<CambioEstadoDTO> listaCambiosEstado;
}
