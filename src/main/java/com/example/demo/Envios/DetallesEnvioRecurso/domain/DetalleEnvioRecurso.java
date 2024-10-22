package com.example.demo.Envios.DetallesEnvioRecurso.domain;

import com.example.demo.Envios.DetallesEnvioRecurso.dto.DetalleEnvioRecursoResponseDTO;
import com.example.demo.Envios.Envios.domain.Envio;
import com.example.demo.Existencias.domain.Existencia;
import com.example.demo.Recursos.domain.Recurso;
import com.example.demo.Recursos.mapper.RecursoMapper;
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
@Table(name = "detallesEnvioRecurso")

public class DetalleEnvioRecurso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iddetalleR")
    private Long idDetalleRecurso;



    @Column(name = "cantidad")
    private Long cantidad;

    //@Column(name = "idrecurso")
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idExistencia")
    private Existencia existencia;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idrecurso")
    private Recurso recurso;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "idenvio", referencedColumnName = "idenvio")
    private Envio envio;

    public DetalleEnvioRecursoResponseDTO toDetalleEnvioRecursoResponseDTO(){
        return DetalleEnvioRecursoResponseDTO.builder()
                //.recursoDTO(RecursoMapper.toDTO(this.existencia.getRecurso()))
                .existenciaDTO(this.existencia.toDto())
                .cantidad(this.cantidad)
                .idDetalleRecurso(this.idDetalleRecurso)
                .build();
    }
}