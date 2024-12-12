package com.example.demo.Reportes.service;

import com.example.demo.Envios.Envios.domain.Envio;
import com.example.demo.Envios.Envios.repository.EnvioRepository;
import com.example.demo.Recursos.domain.RegistroIncorporacion;
import com.example.demo.Recursos.repository.RegistroIncorporacionRepository;
import com.example.demo.Reportes.dto.ReporteCompletoDTO;
import lombok.AllArgsConstructor;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@AllArgsConstructor
public class ReportesService {
    @Autowired
    private EnvioRepository envioRepository;
    @Autowired
    private RegistroIncorporacionRepository registroIncorporacionRepository;

    public ReporteCompletoDTO conseguirReporteCompleto(LocalDate fechaInicio, LocalDate fechaFin) throws Exception {

        ReporteCompletoDTO.ReporteCompletoDTOBuilder builder = ReporteCompletoDTO.builder();
        builder.fechaFin(fechaFin);
        builder.fechaInicio(fechaInicio);

        // primera parte: listado tranqui
        List<Envio> envios = envioRepository.findByFechaPreparacionBetween(fechaInicio, fechaFin);
        List<RegistroIncorporacion> registroIncorporaciones = registroIncorporacionRepository.findByFechaIncBetween(fechaInicio, fechaFin);

        Long pedidosAtendidos = (long) envios.size();

        // emtregados y devueltos
        Long pedidosCompletados = envios.stream().filter(x -> {
                    Long ultimoEstado = x.getListaCambiosEstado()
                            .stream()
                            .filter(ce -> ce.getFechaFin() == null)
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
                            .filter(ce -> ce.getFechaFin() == null)
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
                            .filter(ce -> ce.getFechaFin() == null)
                            .findFirst()
                            .get()
                            .getIdEstadoEnvio();

                    return ultimoEstado != 7L;
                }).toList();


        // buscar los meses entre las dos fechas
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMM/uuuu", Locale.ROOT);
        List<YearMonth> meses = new ArrayList<>();
        for (YearMonth month = YearMonth.from(fechaInicio);
             !month.isAfter(YearMonth.from(fechaFin));
             month = month.plusMonths(1)) {
            meses.add(month);
        }

        //tiempos promedio de procesamiento
        List<Float> tiemposPromedioProcesamiento = new ArrayList<>();

        for (YearMonth mes : meses) {
            float acumulado = 0;
            long cantidad = 0L;

            for (Envio env : enviosSinCancelar) {
                cantidad += 1L;
                acumulado += env.calcularTiempoDeProcesamiento();
            }

            tiemposPromedioProcesamiento.add(cantidad / (float) acumulado);
            //continuar
        }

        builder.meses(meses.stream().map(mes -> mes.format(monthFormatter)).toList());
        builder.tiemposPromedioProcesamiento(tiemposPromedioProcesamiento);

        // estados
        // pendiente 1, en preparacion 2, en reparacion 9, para retiro 8
        List<String> estados = new ArrayList<>(); //List.of("Pendiente", "En preparación", "En reparación", "Para retiro");
        List<Float> tiempoPorEstado = new ArrayList<>();
        HashMap<String, Long> mapaEstados = new HashMap<>();

        mapaEstados.put("Pendiente", 1L);
        mapaEstados.put("En preparación", 2L);
        mapaEstados.put("En reparación", 9L);
        mapaEstados.put("Para retiro", 8L);

        for (Map.Entry<String, Long> entry: mapaEstados.entrySet()) {
            estados.add(entry.getKey());
            Float tiempo = 0f;
            Long cantidad = 0L;
            for (Envio env : enviosSinCancelar){
                Float tEnvio = env.calcularTiempoPorEstado(entry.getValue());
                tiempo += tEnvio;
                cantidad += tEnvio == 0f ? 0L : 1L;
            }

            tiempoPorEstado.add(tiempo / (float) cantidad);
        }

        builder.estados(estados);
        builder.tiemposPromedioEstado(tiempoPorEstado);

        List<String> elementos = new ArrayList<>();
        List<Long> cantidadElem = new ArrayList<>();

        // contar computadoras
        Long cantPc = 0L;
        for (Envio env : enviosSinCancelar){
            cantPc += env.contarPc();
        }
        elementos.add("PC");
        cantidadElem.add(cantPc);

        Long cantNotebooks = 0L;
        for (Envio env: enviosSinCancelar){
            cantNotebooks += env.contarNotebooks();
        }
        elementos.add("Notebooks");
        cantidadElem.add(cantNotebooks);

        Long cantAIO = 0L;
        for (Envio env : enviosSinCancelar){
            cantAIO += env.contarAllInOne();
        }
        elementos.add("All In One");
        cantidadElem.add(cantAIO);

        // TODO contar recursos
        Long cantPerif = 0L;
        for (Envio env : enviosSinCancelar){
            cantPerif += env.contarPerifericos();
        }
        elementos.add("Periféricos");
        cantidadElem.add(cantPerif);

        Long cantComponentes = 0L;
        for (Envio env : enviosSinCancelar){
            cantComponentes += env.contarComponentes();
        }
        elementos.add("Componentes");
        cantidadElem.add(cantComponentes);

        builder.elementos(elementos);
        builder.cantidades(cantidadElem);

        return builder.build();
    }





}
