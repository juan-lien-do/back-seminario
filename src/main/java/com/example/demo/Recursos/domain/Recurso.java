package com.example.demo.Recursos.domain;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Entity
@Table(name = "recursos")

public class Recurso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "idRecurso")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "cantidad")
    private Long cantidad;

    @Column(name = "categoria")
    private Long categoria;

    @Column(name = "descripcion")
    private String descripcion;
}
