package com.example.demo.Recursos.repository;

import com.example.demo.Empleados.domain.Empleado;
import com.example.demo.Recursos.domain.Recurso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecursoRepository extends JpaRepository<Recurso, Long> {
    List<Recurso> findByCategoria(Long categoria);
    List<Recurso> findByActivo(Boolean activo);
}