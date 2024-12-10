package com.example.demo.Usuarios.dto;

import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Accessors(makeFinal = true)
@ToString
public class UsuarioDTO {
    String nombre;
    String apellido_usr;
    String nombre_usr;
    String cuil;
    String email;
    String telefono;
    Long id;
    Boolean isAdmin;
    Boolean esActivo;
    Boolean esDriver;
    Boolean primerLogin;
    String observaciones;
    //Fechas
    LocalDateTime fechaBaja;
    LocalDateTime ultimaActualizacion;
    LocalDateTime fechaCreacion;
}
