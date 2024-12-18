package com.example.demo.Recursos.mapper;

import com.example.demo.Recursos.domain.Recurso;
import com.example.demo.Recursos.dto.RecursoDTO;

public class RecursoMapper {

    public static RecursoDTO toDTO(Recurso recurso){

        return RecursoDTO.builder()
                .id(recurso.getId())
                .nombre(recurso.getNombre())
                .cantidad(recurso.getCantidad())
                .cantidadCritica(recurso.getCantidadCritica())
                .categoria(recurso.getCategoria())
                .descripcion(recurso.getDescripcion())
                .activo(recurso.getActivo())
                .existencias(recurso.getExistencias())
                .build();
    }

    public static Recurso toEntity(RecursoDTO recursoDTO){

        return Recurso.builder()
                .id(recursoDTO.getId())
                .nombre(recursoDTO.getNombre())
                .cantidad(recursoDTO.getCantidad())
                .cantidadCritica(recursoDTO.getCantidadCritica())
                .categoria(recursoDTO.getCategoria())
                .descripcion(recursoDTO.getDescripcion())
                .activo(recursoDTO.getActivo())
                .build();
    }
}
