package com.example.demo.Empleados.domain;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "empleados")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

public class Empleado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idempleado")
    private Long idEmpleado;
    @Column(name = "cuil")
    private Long cuil;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "mail")
    private String mail;
    @Column(name = "telefono")
    private String telefono;
    @Column(name = "activo")
    private Boolean activo;
    @Column(name = "ws")
    private String ws;
    @Column(name = "deleteDate")
    private LocalDate deleteDate;

}
