package com.example.demo.Usuarios.dto;

import lombok.*;
import lombok.experimental.Accessors;

@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Accessors(makeFinal = true)
@ToString
public class UsuarioModificacionDTO {
        String nombre_usr;
        String apellido_usr;
        String cuil;
        String observaciones;
        String mail;
        String telefono;
        Boolean isAdmin;
        Boolean isDriver;
}
