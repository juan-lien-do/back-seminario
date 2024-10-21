package com.example.demo.Empleados.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmpleadoResponseDTO {
    private Long idEmpleado;
    private Long cuil;
    private String nombre;
    private String ws;
    private String mail;
    private String telefono;
    private Boolean activo;
    private LocalDate deleteDate;
}
