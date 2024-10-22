package com.example.demo.Existencias.dto;

import com.example.demo.Depositos.domain.Deposito;
import com.example.demo.Recursos.domain.Recurso;
import lombok.*;
import lombok.experimental.Accessors;

@Builder
@AllArgsConstructor
@Getter
@Setter
@Accessors(makeFinal = true)
@ToString
public class ExistenciaResponseDTO {
    private Long id;
    private Long cantidad;
    private String nombreRecurso;
    private String nombreDeposito;
}
