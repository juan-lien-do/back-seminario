package com.example.demo.Envios.Envios.domain;

import com.example.demo.Envios.CambiosEstadoEnvio.domain.CambioEstadoEnvio;
import com.example.demo.Envios.DetallesEnvioComputadora.domain.DetalleEnvioComputadora;
import com.example.demo.Envios.DetallesEnvioRecurso.domain.DetalleEnvioRecurso;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Entity
@Table(name = "envios")

public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idenvio")
    private Long idEnvio;

    @Column(name = "idempleado")
    private Long idEmpleado;

    @Column(name = "fechapreparacion")
    private Date fechaPreparacion;

    @OneToMany(mappedBy = "envio")
    @JsonManagedReference
    private List<DetalleEnvioComputadora> detallesEnvioComputadora;

    @OneToMany(mappedBy = "envio")
    @JsonManagedReference
    private List<DetalleEnvioRecurso> detallesEnvioRecurso;

    @OneToMany(mappedBy = "envio")
    @JsonManagedReference
    private List<CambioEstadoEnvio> listaCambiosEstado;
}
