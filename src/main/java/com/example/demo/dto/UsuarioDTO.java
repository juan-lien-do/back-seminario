package com.example.demo.dto;

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
    String email;
    String password;
    Long id;
    Boolean isAdmin;
}
