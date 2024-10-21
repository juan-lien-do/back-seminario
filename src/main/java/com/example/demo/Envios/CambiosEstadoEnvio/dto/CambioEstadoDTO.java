package com.example.demo.Envios.CambiosEstadoEnvio.dto;

import com.example.demo.Envios.Envios.domain.Envio;
import lombok.*;
import lombok.experimental.Accessors;
import java.util.Date;

@Builder
@AllArgsConstructor
@Getter
@Setter
@Accessors(makeFinal = true)
@ToString

public class CambioEstadoDTO {
    Long idCambioEstado;
    Date fechaInicio;
    Date fechaFin;
    Long idEstadioEnvio;
    Envio envio;
}
