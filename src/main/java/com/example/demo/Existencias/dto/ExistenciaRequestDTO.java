package com.example.demo.Existencias.dto;

import lombok.*;
import lombok.experimental.Accessors;

@Builder
@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ExistenciaRequestDTO {
    private Long cantidad;
    private Long idRecurso;
    private Long idDeposito;
}
