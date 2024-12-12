package com.example.demo.Reportes.dto;


import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class ReporteCompletoDTO {
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Long pedidosAtendidos;
    private Long pedidosCompletados;
    private Long pedidosEnProceso;
    private Long incorporaciones;

    private List<String> meses;
    private List<Float> tiemposPromedioProcesamiento;
    private List<String> estados;
    private List<Float> tiemposPromedioEstado;
    private List<String> elementos;
    private List<Long> cantidades;
}
