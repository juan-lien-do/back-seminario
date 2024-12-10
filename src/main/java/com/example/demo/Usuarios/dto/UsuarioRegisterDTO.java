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
public class UsuarioRegisterDTO {
        String nombre_usr;
        String apellido_usr;
        String observaciones;
        String mail;
        String telefono;
}
