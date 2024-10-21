package com.example.demo.Envios.DetallesEnvioComputadora.dto;

import com.example.demo.Envios.Envios.domain.Envio;
import lombok.*;
import lombok.experimental.Accessors;


@Builder
@AllArgsConstructor
@Getter
@Setter
@Accessors(makeFinal = true)
@ToString

public class DetalleEnvioComputadoraDTO {
    Long idDetalleComputadora;
    Long idComputadora;
    Envio envio;
}
