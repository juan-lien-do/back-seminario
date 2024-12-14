package com.example.demo.Envios.DetallesEnvioRecurso.repository;

import com.example.demo.Envios.DetallesEnvioRecurso.domain.DetalleEnvioRecurso;
import com.example.demo.Recursos.domain.Recurso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleEnvioRecursoRepository extends JpaRepository<DetalleEnvioRecurso, Long> {
    List<DetalleEnvioRecurso> findByRecurso(Recurso recurso);
}
