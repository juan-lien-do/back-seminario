package com.example.demo.Envios.CambiosEstadoEnvio.repository;

import com.example.demo.Envios.CambiosEstadoEnvio.domain.CambioEstadoEnvio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CambiosEstadoEnvioRepository extends JpaRepository<CambioEstadoEnvio, Long> {
}
