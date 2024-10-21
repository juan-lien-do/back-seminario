package com.example.demo.Envios.EstadosEnvio.dto;

import lombok.*;
import lombok.experimental.Accessors;


@Builder
@AllArgsConstructor
@Getter
@Setter
@Accessors(makeFinal = true)
@ToString

public class EstadosEnvioDTO {
    Long idEstado;
    String nombre;
    String descripcion;
}
