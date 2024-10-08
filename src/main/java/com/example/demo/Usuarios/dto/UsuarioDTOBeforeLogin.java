package com.example.demo.Usuarios.dto;

import lombok.*;
import lombok.experimental.Accessors;

@Builder
@AllArgsConstructor
@Getter
@Setter
@Accessors(makeFinal = true)
@ToString
public class UsuarioDTOBeforeLogin {
    String nombre;
    String password;
}
