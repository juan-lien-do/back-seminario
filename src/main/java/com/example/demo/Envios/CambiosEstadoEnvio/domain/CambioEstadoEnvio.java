package com.example.demo.Envios.CambiosEstadoEnvio.domain;

import com.example.demo.Envios.CambiosEstadoEnvio.dto.CambioEstadoDTO;
import com.example.demo.Envios.Envios.domain.Envio;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Entity
@Table(name = "cambiosEstadoEnvio")

public class CambioEstadoEnvio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcambioestado")
    private Long idCambioEstado;

    @Column(name = "fechainicio")
    private LocalDateTime fechaInicio;

    @Column(name = "fechafin")
    private LocalDateTime fechaFin;

    @Column(name = "idestadoenvio")
    private Long idEstadoEnvio;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "idenvio", referencedColumnName = "idenvio")
    private Envio envio;

    public CambioEstadoDTO toDTO(){
        return CambioEstadoDTO.builder()
                .idEstadoEnvio(this.idEstadoEnvio)
                .fechaInicio(this.fechaInicio)
                .fechaFin(this.fechaFin)
                .idCambioEstado(this.idCambioEstado)
                .build();
    }
}
