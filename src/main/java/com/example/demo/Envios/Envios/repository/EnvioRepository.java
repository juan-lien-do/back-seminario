package com.example.demo.Envios.Envios.repository;

import com.example.demo.Envios.Envios.domain.Envio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EnvioRepository extends JpaRepository<Envio, Long> {
    List<Envio> findByFechaPreparacionBetween(LocalDate fechaInicio, LocalDate fechaFin);


    Optional<Envio> findEnvioByIdEnvio(Long idEnvio);



}
