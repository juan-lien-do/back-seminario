package com.example.demo.Envios.Envios.mapper;

import com.example.demo.Envios.Envios.domain.Envio;
import com.example.demo.Envios.Envios.dto.EnvioDTO;

public class EnvioMapper {

    public static EnvioDTO toDTO(Envio envio){

        return EnvioDTO.builder()
                .idEnvio(envio.getIdEnvio())
                .idEmpleado(envio.getEmpleado().getIdEmpleado())
                .fechaPreparacion(envio.getFechaPreparacion())
                .detallesEnvioComputadora(envio.getDetallesEnvioComputadora())
                .detallesEnvioRecurso(envio.getDetallesEnvioRecurso())
                .listaCambiosEstado(envio.getListaCambiosEstado())
                .build();
    }

    public static Envio toEntity(EnvioDTO envioDTO){

        return Envio.builder()
                .idEnvio(envioDTO.getIdEnvio())
                .fechaPreparacion(envioDTO.getFechaPreparacion())
                .detallesEnvioComputadora(envioDTO.getDetallesEnvioComputadora())
                .detallesEnvioRecurso(envioDTO.getDetallesEnvioRecurso())
                .listaCambiosEstado(envioDTO.getListaCambiosEstado())
                .build();
    }
}
