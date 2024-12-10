package com.example.demo.Envios.DetallesEnvioComputadora.domain;

import com.example.demo.Computadoras.domain.Computadora;
import com.example.demo.Computadoras.mapper.ComputadoraMapper;
import com.example.demo.Envios.DetallesEnvioComputadora.dto.DetalleEnvioComputadoraResponseDTO;
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
@Table(name = "detallesEnvioComputadora")

public class DetalleEnvioComputadora {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iddetalleC")
    private Long idDetalleComputadora;

    //@Column(name = "idcomputadora")
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idcomputadora")
    private Computadora computadora;

    @Column(name = "es_devuelto")
    private Boolean esDevuelto;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "idenvio", referencedColumnName = "idenvio")
    private Envio envio;


    public DetalleEnvioComputadoraResponseDTO toResponseDTO(){
        return DetalleEnvioComputadoraResponseDTO.builder()
                .computadoraDTO(ComputadoraMapper.toDTO(this.computadora))
                .idDetalleComputadora(this.idDetalleComputadora)
                .build();
    }
}
