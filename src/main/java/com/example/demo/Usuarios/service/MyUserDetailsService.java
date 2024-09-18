package com.example.demo.Usuarios.service;

import com.example.demo.Usuarios.repository.UsuarioRepository;
import com.example.demo.Usuarios.domain.Usuario;
import com.example.demo.Usuarios.domain.UsuarioPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        Optional<Usuario> usuario = usuarioRepository.findByNombre(username);
        if (usuario.isEmpty()){
            System.out.println("user not found");
            throw new UsernameNotFoundException("user not found");
        }
        return new UsuarioPrincipal(usuario.get());
    }
}
