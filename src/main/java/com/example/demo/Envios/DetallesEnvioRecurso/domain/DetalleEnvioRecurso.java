package com.example.demo.Envios.DetallesEnvioRecurso.domain;

import com.example.demo.Envios.Envios.domain.Envio;
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
@Table(name = "detallesenviorecurso")

public class DetalleEnvioRecurso {

    @Id
    @Column(name = "iddetalleR")
    private String idDetalleRecurso;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "idrecurso")
    private int idRecurso;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "idenvio", referencedColumnName = "idenvio")
    private Envio envio;

}