package com.example.demo.Envios.Envios.dto;

import com.example.demo.Envios.DetallesEnvioComputadora.dto.DetalleEnvioComputadoraPostDTO;
import com.example.demo.Envios.DetallesEnvioRecurso.dto.DetalleRecursoPostDTO;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EnvioPostDTO {
    Long idEmpleado;
    String nombreUsuario;
    List<DetalleRecursoPostDTO> detallesEnvioRecurso;
    List<DetalleEnvioComputadoraPostDTO> detallesEnvioComputadora;


}
