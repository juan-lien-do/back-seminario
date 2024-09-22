package com.example.demo.Existencias.dto;

import com.example.demo.Depositos.domain.Deposito;
import com.example.demo.Recursos.domain.Recurso;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.Accessors;

@Builder
@AllArgsConstructor
@Getter
@Setter
@Accessors(makeFinal = true)
@ToString

public class ExistenciaDTO {
    private String id;
    private int cantidad;
    private Recurso recurso;
    private Deposito deposito;
}
