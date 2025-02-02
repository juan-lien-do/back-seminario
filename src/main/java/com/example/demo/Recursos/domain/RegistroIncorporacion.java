package com.example.demo.Recursos.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

@Entity
@Table(name = "registro_incorporaciones")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistroIncorporacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "cantidad")
    private Long cantidad;
    @Column(name = "fechaInc")
    private LocalDate fechaInc;
    @Column(name = "idRecurso") // por ahora no necesitamos esto
    private Long idRecurso;
}
