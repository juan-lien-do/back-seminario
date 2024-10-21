package com.example.demo.Envios.DetallesEnvioComputadora.domain;

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
@Table(name = "detallesenviocomputadora")

public class DetalleEnvioComputadora {

    @Id
    @Column(name = "iddetalleC")
    private String idDetalleComputadora;

    @Column(name = "idcomputadora")
    private int idComputadora;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "idenvio", referencedColumnName = "idenvio")
    private Envio envio;
}
