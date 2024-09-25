package com.example.demo.Depositos.dto;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

@Builder
@AllArgsConstructor
@Getter
@Setter
@Accessors(makeFinal = true)
@ToString

public class DepositoDTO {
    Long id;
    String nombre;
    String ubicacion;
}
