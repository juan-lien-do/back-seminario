package com.example.demo.Empleados.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmpleadoRequestDTO {

    private Long cuil;
    @NotEmpty
    private String nombre;
    private String mail;
    private String telefono;
}
