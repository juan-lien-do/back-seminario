package com.example.demo.service;

import com.example.demo.domain.Usuario;
import com.example.demo.domain.UsuarioPrincipal;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
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
