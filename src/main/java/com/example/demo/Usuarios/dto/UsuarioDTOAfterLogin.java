package com.example.demo.Usuarios.dto;

import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Getter
@Setter
@Accessors(makeFinal = true)
@ToString
public class UsuarioDTOAfterLogin {
    String nombre;
    String token;
    Boolean isAdmin;
    Boolean primerLogin;
    LocalDateTime fechaUltimaModificacion;
}
