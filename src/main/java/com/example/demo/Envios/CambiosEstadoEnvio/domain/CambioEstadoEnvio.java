package com.example.demo.Envios.CambiosEstadoEnvio.domain;

import com.example.demo.Envios.CambiosEstadoEnvio.dto.CambioEstadoDTO;
import com.example.demo.Envios.Envios.domain.Envio;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

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

    public CambioEstadoDTO toDTO() {
        return CambioEstadoDTO.builder()
                .idEstadoEnvio(this.idEstadoEnvio)
                .fechaInicio(this.fechaInicio)
                .fechaFin(this.fechaFin)
                .idCambioEstado(this.idCambioEstado)
                .build();
    }

    public Boolean esDeProcesamiento() {
        return idEstadoEnvio == 1L || idEstadoEnvio == 2L || idEstadoEnvio == 3L || idEstadoEnvio == 9L || idEstadoEnvio == 8L;
    }

    public Float calcularTiempoProcesamiento() {
        if (esDeProcesamiento()) return calcularTiempo();
        else return 0f;
    }

    public Float calcularTiempo(){ // devuelve el tiempo en horas con coma
        // HAY QUE USAR MINUTES porque sino devuelve una cantidad entera de horas
        return ((float) ChronoUnit.MINUTES.between(fechaInicio, fechaFin != null ? fechaFin : LocalDateTime.now())) / 60f;
    }

    public Boolean sosEstado(Long estado){
        return Objects.equals(estado, idEstadoEnvio);
    }
    public Boolean sosFinal(){
        return fechaFin == null;
    }


}
