package com.example.demo.Usuarios.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "nombre")
    private String nombre;
    //datos del usuario
    @Column(name = "nombre_usr")
    private String nombreUsuario;
    @Column(name = "apellido_usr")
    private String apellidoUsuario;
    @Column(name = "cuil")
    private String cuil;
    @Column(name = "mail")
    private String mail;
    @Column(name = "password")
    private String password;
    @Column(name = "is_admin")
    private Boolean isAdmin;
    @Column(name = "is_driver")
    private Boolean isDriver;
    @Column(name = "es_activo")
    private Boolean esActivo;
    @Column(name= "telefono")
    private String telefono;
    @Column(name = "primer_login")
    private Boolean primerLogin;
    @Column(name = "observaciones")
    private String observaciones;
    @Column(name = "fecha_baja")
    private LocalDateTime fechaBaja;
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
    @Column(name = "ultima_actualizacion")
    private LocalDateTime ultimaActualizacion;

}
