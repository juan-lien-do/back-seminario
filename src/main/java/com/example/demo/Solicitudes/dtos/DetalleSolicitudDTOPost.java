package com.example.demo.Solicitudes.dtos;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class DetalleSolicitudDTOPost {
    private Long cantidad;
    private String nombre;
    private String descripcion;
}