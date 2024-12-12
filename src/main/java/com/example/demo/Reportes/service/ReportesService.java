package com.example.demo.Reportes.service;

import com.example.demo.Envios.CambiosEstadoEnvio.domain.CambioEstadoEnvio;
import com.example.demo.Envios.Envios.domain.Envio;
import com.example.demo.Envios.Envios.repository.EnvioRepository;
import com.example.demo.Envios.Envios.service.EnvioService;
import com.example.demo.Recursos.domain.RegistroIncorporacion;
import com.example.demo.Recursos.repository.RegistroIncorporacionRepository;
import com.example.demo.Reportes.dto.ReporteCompletoDTO;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class ReportesService {
    @Autowired
    private EnvioRepository envioRepository;
    @Autowired
    private RegistroIncorporacionRepository registroIncorporacionRepository;

    public ReporteCompletoDTO conseguirReporteCompleto(Date fi, Date ff) throws Exception{
        LocalDate fechaInicio = fi.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate fechaFin = ff.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        ReporteCompletoDTO.ReporteCompletoDTOBuilder builder = ReporteCompletoDTO.builder();
        builder.fechaFin(fechaFin);
        builder.fechaInicio(fechaInicio);

        // primera parte: listado tranqui
        List<Envio> envios = envioRepository.findByDateBetween(fechaInicio, fechaFin);
        List<RegistroIncorporacion> registroIncorporaciones = registroIncorporacionRepository.findByDateBetween(fechaInicio, fechaFin);

        Long pedidosAtendidos = (long) envios.size();

        // emtregados y devueltos
        Long pedidosCompletados = envios.stream().filter(x -> {
                    Long ultimoEstado = x.getListaCambiosEstado()
                            .stream()
                            .filter( ce -> ce.getFechaFin() == null)
                            .findFirst()
                            .get()
                            .getIdEstadoEnvio();

                    return ultimoEstado == 4L || ultimoEstado == 5L || ultimoEstado == 6L;
                })
                        .count();


        // ni entregados, ni devueltos, ni cancelados
        Long pedidosEnProceso = envios.stream().filter(
                x -> {
                    Long ultimoEstado = x.getListaCambiosEstado()
                            .stream()
                            .filter( ce -> ce.getFechaFin() == null)
                            .findFirst()
                            .get()
                            .getIdEstadoEnvio();

                    return ultimoEstado == 8L || ultimoEstado == 9L ||
                            ultimoEstado == 3L || ultimoEstado == 2L ||
                            ultimoEstado == 1L;
                }).count();

        Long incorporaciones = (long) (registroIncorporaciones == null ? 0L : registroIncorporaciones.size());

        builder.pedidosAtendidos(pedidosAtendidos);
        builder.pedidosCompletados(pedidosCompletados);
        builder.pedidosEnProceso(pedidosEnProceso);
        builder.incorporaciones(incorporaciones);

        List<Envio> enviosSinCancelar = envios.stream().filter(
                x -> {
                    Long ultimoEstado = x.getListaCambiosEstado()
                            .stream()
                            .filter( ce -> ce.getFechaFin() == null)
                            .findFirst()
                            .get()
                            .getIdEstadoEnvio();

                    return ultimoEstado != 7L;
                }).toList();







        return builder.build();
    }
}
