package com.example.demo.Solicitudes.domain;

import com.example.demo.Usuarios.domain.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "solicitudes")
@ToString
public class Solicitud {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idSolicitud")
    private Long idSolicitud;
    @Column(name = "fechaSolicitud")
    private LocalDate fechaSolicitud;
    @Column(name = "fechaIncorporacion")
    private LocalDate fechaIncorporacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuarioId")
    private Usuario usuario;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "solicitud")
    private List<DetalleSolicitud> detallesSolicitud;
}
