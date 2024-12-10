package com.example.demo.Recursos.domain;

import com.example.demo.Existencias.domain.Existencia;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;

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
    @Column(name = "idrecurso")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "cantidad")
    private Long cantidad;

    @Column(name = "cantidadCritica")
    private Long cantidadCritica;

    @Column(name = "categoria")
    private Long categoria;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "activo")
    private Boolean activo;

    @OneToMany(mappedBy = "recurso", fetch = FetchType.EAGER, cascade = CascadeType.ALL)

    @JsonManagedReference
    //Borrar si se rompe todo xd, es para "rendimiento"
    @Fetch(FetchMode.JOIN)
    private List<Existencia> existencias;
}
