package com.example.demo.Usuarios.dto;

import lombok.*;
import lombok.experimental.Accessors;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Accessors(makeFinal = true)
public class UsuarioPassDTO {
    private String nombre;
    private String nuevaPassword;
}
