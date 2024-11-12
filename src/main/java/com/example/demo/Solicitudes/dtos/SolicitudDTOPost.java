package com.example.demo.Solicitudes.dtos;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class SolicitudDTOPost {
    private String nombreUsuario;
    private List<DetalleSolicitudDTOPost> detallesSolicitud;
}
