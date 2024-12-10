package com.example.demo.Computadoras.mapper;

import com.example.demo.Computadoras.domain.Computadora;
import com.example.demo.Computadoras.dto.ComputadoraDTO;

public class ComputadoraMapper {

    public static ComputadoraDTO toDTO(Computadora computadora){

        return ComputadoraDTO.builder()
                .idComputadora(computadora.getIdComputadora())
                .idTipo(computadora.getIdTipo())
                .idDeposito(computadora.getIdDeposito())
                .nroSerie(computadora.getNroSerie())
                .descripcion(computadora.getDescripcion())
                .esActivo(computadora.getEsActivo())
                .esMasterizado(computadora.getEsMasterizado())
                .nroWs(computadora.getNroWs())
                .enUso(computadora.getEnUso())
                .build();
    }

    public static Computadora toEntity(ComputadoraDTO depositoDTO){

        return Computadora.builder()
                .idComputadora(depositoDTO.getIdComputadora())
                .idTipo(depositoDTO.getIdTipo())
                .idDeposito(depositoDTO.getIdDeposito())
                .nroSerie(depositoDTO.getNroSerie())
                .descripcion(depositoDTO.getDescripcion())
                .esActivo(depositoDTO.getEsActivo())
                .esMasterizado(depositoDTO.getEsMasterizado())
                .nroWs(depositoDTO.getNroWs())
                .enUso(depositoDTO.getEnUso())
                .build();
    }
}
