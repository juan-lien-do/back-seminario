package com.example.demo.Envios.DetallesEnvioRecurso.dto;

import lombok.*;
import lombok.experimental.Accessors;

@Builder
@AllArgsConstructor
@Getter
@Setter
@Accessors(makeFinal = true)
@ToString
public class DetalleRecursoPostDTO {
    Long cantidad;
    Long idRecurso;
    Long idExistencia;
}
