package com.example.demo.Envios.Envios.domain;

import com.example.demo.Empleados.domain.Empleado;
import com.example.demo.Envios.CambiosEstadoEnvio.domain.CambioEstadoEnvio;
import com.example.demo.Envios.DetallesEnvioComputadora.domain.DetalleEnvioComputadora;
import com.example.demo.Envios.DetallesEnvioRecurso.domain.DetalleEnvioRecurso;
import com.example.demo.Envios.Envios.dto.EnvioResponseDTO;
import com.example.demo.Usuarios.domain.Usuario;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
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

    //@Column(name = "idempleado")
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idempleado")
    private Empleado empleado;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idUsuario")
    private Usuario usuario;

    @Column(name = "fechapreparacion")
    private LocalDate fechaPreparacion;

    @OneToMany(mappedBy = "envio")
    @JsonManagedReference
    private List<DetalleEnvioComputadora> detallesEnvioComputadora;

    @OneToMany(mappedBy = "envio")
    @JsonManagedReference
    private List<DetalleEnvioRecurso> detallesEnvioRecurso;

    @OneToMany(mappedBy = "envio")
    @JsonManagedReference
    private List<CambioEstadoEnvio> listaCambiosEstado;


    public EnvioResponseDTO toResponseDTO() {
        return EnvioResponseDTO.builder()
                .cuilEmpleado(this.empleado.getCuil())
                .nombreEmpleado(this.empleado.getNombre())
                .idEnvio(this.idEnvio)
                .nombreUsuario(this.usuario.getNombre())
                .fechaPreparacion(this.fechaPreparacion)
                .listaCambiosEstado(this.listaCambiosEstado.stream().map(CambioEstadoEnvio::toDTO).toList())
                .detallesEnvioComputadora(this.detallesEnvioComputadora.stream().map(DetalleEnvioComputadora::toResponseDTO).toList())
                .detallesEnvioRecurso(this.detallesEnvioRecurso.stream().map(DetalleEnvioRecurso::toDetalleEnvioRecursoResponseDTO).toList())
                .build();
    }

    public CambioEstadoEnvio conseguirUltimoEstado(){
        for (CambioEstadoEnvio cev : listaCambiosEstado){
            if (cev.sosFinal()){
                return cev;
            }
        }

        return listaCambiosEstado.get(listaCambiosEstado.size()-1);
    }

    // TODO funcion para calcular tiempo por estado
    public Float calcularTiempoDeProcesamiento(){
        Float tiempo = 0f;
        for (CambioEstadoEnvio cev :
                listaCambiosEstado) {
            tiempo += cev.calcularTiempoProcesamiento();
        }
        return tiempo;
    }

    public Float calcularTiempoPorEstado(Long estado){
        Float tiempo = 0f;
        for (CambioEstadoEnvio cev :
                listaCambiosEstado) {
            if (cev.sosEstado(estado)) {
                tiempo += cev.calcularTiempo();
            }
        }

        return tiempo;
    }
    
    public Long contarNotebooks(){
        Long cantidad = 0L;
        
        if (detallesEnvioComputadora == null) return 0L;
        else {
            for (DetalleEnvioComputadora det :
                    detallesEnvioComputadora) {
                if (det.getComputadora().getIdTipo() == 1L)
                    cantidad += 1;
            }
        }
        return cantidad;
    }

    public Long contarPc(){
        Long cantidad = 0L;

        if (detallesEnvioComputadora == null) return 0L;
        else {
            for (DetalleEnvioComputadora det :
                    detallesEnvioComputadora) {
                if (det.getComputadora().getIdTipo() == 2L)
                    cantidad += 1;
            }
        }
        return cantidad;
    }

    public Long contarAllInOne(){
        Long cantidad = 0L;

        if (detallesEnvioComputadora == null) return 0L;
        else {
            for (DetalleEnvioComputadora det :
                    detallesEnvioComputadora) {
                if (det.getComputadora().getIdTipo() == 3L)
                    cantidad += 1;
            }
        }
        return cantidad;
    }

    public Long contarPerifericos(){
        Long cantidad = 0L;

        if(detallesEnvioRecurso == null) return 0L;
        else {
            for (DetalleEnvioRecurso der : detallesEnvioRecurso){
                if (der.getRecurso().getCategoria() == 1L){
                    cantidad += der.getCantidad();
                }
            }
        }
        return cantidad;
    }
    public Long contarComponentes(){
        Long cantidad = 0L;

        if(detallesEnvioRecurso == null) return 0L;
        else {
            for (DetalleEnvioRecurso der : detallesEnvioRecurso){
                if (der.getRecurso().getCategoria() == 2L){
                    cantidad += der.getCantidad();
                }
            }
        }
        return cantidad;
    }

    // cancelado o devuelto completo
    public Boolean sosFinalizado(){
        CambioEstadoEnvio cev = conseguirUltimoEstado();
        return cev.sosEstado(7L) || cev.sosEstado(6L);
    }
}
