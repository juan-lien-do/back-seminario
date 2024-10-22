package com.example.demo.Envios.DetallesEnvioRecurso.dto;

import com.example.demo.Existencias.dto.ExistenciaDTO;
import com.example.demo.Existencias.dto.ExistenciaResponseDTO;
import com.example.demo.Recursos.dto.RecursoDTO;
import lombok.*;
import lombok.experimental.Accessors;

@Builder
@AllArgsConstructor
@Getter
@Setter
@ToString
public class DetalleEnvioRecursoResponseDTO {
    Long idDetalleRecurso;
    Long cantidad;
    ExistenciaResponseDTO existenciaDTO;
}
