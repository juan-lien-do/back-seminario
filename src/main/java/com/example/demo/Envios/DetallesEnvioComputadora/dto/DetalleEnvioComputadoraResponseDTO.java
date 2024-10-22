package com.example.demo.Envios.DetallesEnvioComputadora.dto;

import com.example.demo.Computadoras.dto.ComputadoraDTO;
import lombok.*;
import lombok.experimental.Accessors;

@Builder
@AllArgsConstructor
@Getter
@Setter
@ToString
public class DetalleEnvioComputadoraResponseDTO {
    private Long idDetalleComputadora;
    private ComputadoraDTO computadoraDTO;
}
