package com.example.demo.Usuarios.mapper;

import com.example.demo.Usuarios.dto.UsuarioDTO;
import com.example.demo.Usuarios.domain.Usuario;

import java.time.LocalDateTime;

public class UsuarioMapper {
    public static UsuarioDTO toDto(Usuario usuario){
        return UsuarioDTO.builder()
                .id(usuario.getId())
                .email(usuario.getMail())
                .nombre(usuario.getNombre())
                .apellido_usr(usuario.getApellidoUsuario())
                .nombre_usr(usuario.getNombreUsuario())
                .password(usuario.getPassword())
                .isAdmin(usuario.getIsAdmin())
                .esDriver(usuario.getIsDriver())
                .telefono(usuario.getTelefono())
                .esActivo(usuario.getEsActivo())
                //fechas
                .fechaBaja(usuario.getFechaBaja())
                .fechaCreacion(usuario.getFechaCreacion())
                .ultimaActualizacion(usuario.getUltimaActualizacion())
                .observaciones(usuario.getObservaciones())
                .build();
    }

    public static Usuario toEntity(UsuarioDTO user) {
        return Usuario.builder()
                .id(user.getId())
                .nombre(user.getNombre())
                .apellidoUsuario(user.getApellido_usr())
                .nombreUsuario(user.getNombre_usr())
                .mail(user.getEmail())
                .password(user.getPassword())
                .isAdmin(user.getIsAdmin())
                .isDriver(user.getEsDriver())
                .telefono(user.getTelefono())
                .esActivo(user.getEsActivo())
                .fechaBaja(user.getFechaBaja())
                .fechaCreacion(user.getFechaCreacion())
                .ultimaActualizacion(user.getUltimaActualizacion())
                .observaciones(user.getObservaciones())
                .build();
    }
}
