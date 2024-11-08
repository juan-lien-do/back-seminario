package com.example.demo.Solicitudes.mapper;

import com.example.demo.Solicitudes.domain.DetalleSolicitud;
import com.example.demo.Solicitudes.domain.Solicitud;
import com.example.demo.Solicitudes.dtos.DetalleSolicitudDTOGet;
import com.example.demo.Solicitudes.dtos.DetalleSolicitudDTOPost;
import com.example.demo.Solicitudes.dtos.SolicitudDTOGet;

public class SolicitudMapper {
    public static SolicitudDTOGet toDTOGet(Solicitud solicitud){
        return SolicitudDTOGet.builder()
                .idSolicitud(solicitud.getIdSolicitud())
                .fechaSolicitud(solicitud.getFechaSolicitud())
                .fechaIncorporacion(solicitud.getFechaIncorporacion())
                .nombreUsuario(solicitud.getUsuario().getNombre())
                .detallesSolicitud(solicitud.getDetallesSolicitud().stream().map(SolicitudMapper::toDTOGet).toList())
                .build();
    }

    public static DetalleSolicitudDTOGet toDTOGet(DetalleSolicitud detalleSolicitud){
        return DetalleSolicitudDTOGet.builder()
                .idDetalleSolicitud(detalleSolicitud.getIdDetalleSolicitud())
                .descripcion(detalleSolicitud.getDescripcion())
                .nombre(detalleSolicitud.getNombre())
                .cantidad(detalleSolicitud.getCantidad())
                .build();
    }

    public static DetalleSolicitud fromDTOPost(DetalleSolicitudDTOPost detalleSolicitudDTOPost, Solicitud solicitud){
        return DetalleSolicitud.builder()
                .solicitud(solicitud)
                .cantidad(detalleSolicitudDTOPost.getCantidad())
                .nombre(detalleSolicitudDTOPost.getNombre())
                .descripcion(detalleSolicitudDTOPost.getDescripcion())
                .build();
    }
}
