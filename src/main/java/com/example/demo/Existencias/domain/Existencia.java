package com.example.demo.Existencias.domain;

import com.example.demo.Recursos.domain.Recurso;
import com.example.demo.Depositos.domain.Deposito;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Entity
@Table(name = "existencias")
public class Existencia {

    @Id
    @Column(name = "idexistencia") // Verifica que este nombre sea correcto
    private String id;

    @Column(name = "cantidad") // Verifica que este nombre sea correcto
    private int cantidad;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "idrecurso", referencedColumnName = "idrecurso") // Asegúrate de que idrecurso sea correcto
    private Recurso recurso;

    @ManyToOne
    @JoinColumn(name = "iddepo", referencedColumnName = "iddeposito") // Asegúrate de que idDeposito sea correcto
    private Deposito deposito;
}
