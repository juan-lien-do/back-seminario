package com.example.demo.Computadoras.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Entity
@Table(name = "computadoras")

public class Computadora {

    @Id
    @Column(name = "idcomputadora")
    private Long idComputadora;

    @Column(name = "idtipo")
    private Long idTipo;

    @Column(name = "iddeposito")
    private Long idDeposito;

    @Column(name = "nroserie", unique = true)
    private String nroSerie;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "esactivo")
    private Boolean esActivo;

    @Column(name = "esmasterizado")
    private Boolean esMasterizado;

    @Column(name = "nrows", unique = true)
    private Long nroWs;
}
