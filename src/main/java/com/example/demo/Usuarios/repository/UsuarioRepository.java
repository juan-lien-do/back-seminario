package com.example.demo.Usuarios.repository;

import com.example.demo.Usuarios.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    public Optional<Usuario> findByNombre(String nombre);
    public Boolean existsByCuil(String cuil);
    public List<Usuario> findByIsDriver(Boolean isDriver);
    public Boolean existsByNombre(String username);
}
