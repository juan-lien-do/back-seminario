package com.example.demo.Recursos.dto;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

@Builder
@AllArgsConstructor
@Getter
@Setter
@Accessors(makeFinal = true)
@ToString

public class RecursoDTO {
    Long id;
    String nombre;
    Long cantidad;
    Long categoria;
    String descripcion;
    Boolean activo;
    List existencias;
    Boolean esDevuelto;
}
