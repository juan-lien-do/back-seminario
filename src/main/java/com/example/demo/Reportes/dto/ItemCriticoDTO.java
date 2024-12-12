package com.example.demo.Reportes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class ItemCriticoDTO {
    private String nombre;
    private Long cantidad;
    private Long cantidadCritica;
}
