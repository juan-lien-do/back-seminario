package com.example.demo.Envios.Envios.dto;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@Builder
@AllArgsConstructor
@Getter
@Setter
@Accessors(makeFinal = true)
@ToString

public class EnvioDTO {
    Long idEnvio;
    Long idEmpleado;
    Date fechaPreparacion;
    List detallesEnvioComputadora;
    List detallesEnvioRecurso;
}