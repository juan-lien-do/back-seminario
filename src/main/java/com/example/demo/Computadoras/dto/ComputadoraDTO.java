package com.example.demo.Computadoras.dto;

import lombok.*;
import lombok.experimental.Accessors;

@Builder
@AllArgsConstructor
@Getter
@Setter
@Accessors(makeFinal = true)
@ToString

public class ComputadoraDTO {

    Long idComputadora;
    Long idTipo;
    Long idDeposito;
    String nroSerie;
    String descripcion;
    Boolean esActivo;
    Boolean esMasterizado;
    Long nroWs;
    Boolean enUso;
}
