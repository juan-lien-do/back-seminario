package com.example.demo.Recursos.repository;

import com.example.demo.Envios.Envios.domain.Envio;
import com.example.demo.Recursos.domain.RegistroIncorporacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RegistroIncorporacionRepository extends JpaRepository<RegistroIncorporacion, Long> {
    List<RegistroIncorporacion> findByDateBetween(LocalDate fechaInicio, LocalDate fechaFin);

}
