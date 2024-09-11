package com.example.demo.mapper;

import com.example.demo.domain.Usuario;
import com.example.demo.dto.UsuarioDTO;

public class UsuarioMapper {
    public static UsuarioDTO toDto(Usuario usuario){
        return UsuarioDTO.builder()
                .id(usuario.getId())
                .email(usuario.getMail())
                .nombre(usuario.getNombre())
                .password(usuario.getPassword())
                .isAdmin(usuario.getIsAdmin())
                .build();
    }

    public static Usuario toEntity(UsuarioDTO user) {
        return Usuario.builder()
                .id(user.getId())
                .nombre(user.getNombre())
                .mail(user.getEmail())
                .password(user.getPassword())
                .isAdmin(user.getIsAdmin())
                .build();
    }
}
