package com.example.demo.Depositos.mapper;

import com.example.demo.Depositos.domain.Deposito;
import com.example.demo.Depositos.dto.DepositoDTO;

public class DepositoMapper {

    public static DepositoDTO toDTO(Deposito deposito){

        return DepositoDTO.builder()
                .id(deposito.getIdDeposito())
                .nombre(deposito.getNombre())
                .ubicacion(deposito.getUbicacion())
                .build();
    }

    public static Deposito toEntity(DepositoDTO depositoDTO){

        return Deposito.builder()
                .idDeposito(depositoDTO.getId())
                .nombre(depositoDTO.getNombre())
                .ubicacion(depositoDTO.getUbicacion())
                .build();
    }
}
