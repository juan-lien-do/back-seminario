package com.example.demo.Envios.DetallesEnvioRecurso.repository;

import com.example.demo.Envios.DetallesEnvioRecurso.domain.DetalleEnvioRecurso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleEnvioRecursoRepository extends JpaRepository<DetalleEnvioRecurso, Long> {
}
