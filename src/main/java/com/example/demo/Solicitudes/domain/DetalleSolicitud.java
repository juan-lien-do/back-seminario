package com.example.demo.Solicitudes.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "detalles_solicitud")
public class DetalleSolicitud {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idDetalleSolicitud")
    private Long idDetalleSolicitud;
    @ManyToOne
    @JoinColumn(name = "solicitud_id")
    private Solicitud solicitud;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "cantidad")
    private Long cantidad;


}
