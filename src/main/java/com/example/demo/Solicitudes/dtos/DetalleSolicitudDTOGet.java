package com.example.demo.Solicitudes.dtos;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class DetalleSolicitudDTOGet {
    private Long idDetalleSolicitud;
    private Long cantidad;
    private String nombre;
    private String descripcion;
}
