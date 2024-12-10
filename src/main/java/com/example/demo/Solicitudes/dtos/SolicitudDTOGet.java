package com.example.demo.Solicitudes.dtos;


import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class SolicitudDTOGet {
    private Long idSolicitud;
    private LocalDate fechaSolicitud;
    private LocalDate fechaIncorporacion;
    private String nombreUsuario;
    private List<DetalleSolicitudDTOGet> detallesSolicitud;
}
