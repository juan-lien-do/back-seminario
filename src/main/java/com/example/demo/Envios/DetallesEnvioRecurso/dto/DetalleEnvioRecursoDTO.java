package com.example.demo.Envios.DetallesEnvioRecurso.dto;

import com.example.demo.Envios.Envios.domain.Envio;
import lombok.*;
import lombok.experimental.Accessors;


@Builder
@AllArgsConstructor
@Getter
@Setter
@Accessors(makeFinal = true)
@ToString

public class DetalleEnvioRecursoDTO {

    Long idDetalleRecurso;
    Long idRecurso;
    Integer cantidad;
    Envio envio;

}
