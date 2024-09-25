package com.example.demo.Empleados.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmpleadoPostRequestDTO {
    @NotNull
    private Long cuil;
    @NotEmpty
    private String nombre;
    private String mail;
    private String ws;
    private String telefono;
}
