package com.example.demo.Usuarios.domain;

import com.example.demo.Envios.Envios.domain.Envio;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "mail")
    private String mail;
    @Column(name = "password")
    private String password;
    @Column(name = "is_admin")
    private Boolean isAdmin;
    @Column(name= "telefono")
    private String telefono;

    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    public List<Envio> envios;
}
