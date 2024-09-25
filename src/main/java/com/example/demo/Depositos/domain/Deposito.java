package com.example.demo.Depositos.domain;

import com.example.demo.Existencias.domain.Existencia;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Entity
@Table(name = "depositos")

public class Deposito {

    @Id
    @Column(name = "iddeposito")
    private Long idDeposito;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "ubicacion")
    private String ubicacion;

    @JsonIgnore
    @OneToMany(mappedBy = "deposito")
    private List<Existencia> existencias;
}

