package com.example.demo.dto;

import lombok.*;
import lombok.experimental.Accessors;

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
}
