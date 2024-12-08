package com.example.demo.Usuarios.dto;

import lombok.*;
import lombok.experimental.Accessors;

@Builder
@AllArgsConstructor
@Getter
@Setter
@Accessors(makeFinal = true)
@ToString
public class UsuarioDTO {
    String nombre;
    String apellido_usr;
    String nombre_usr;
    String email;
    String telefono;
    String password;
    Long id;
    Boolean isAdmin;
    Boolean esActivo;
    Boolean esDriver;
    Boolean primerLogin;
}
