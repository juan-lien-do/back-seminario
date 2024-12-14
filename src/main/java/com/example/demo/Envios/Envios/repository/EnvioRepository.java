package com.example.demo.Envios.Envios.repository;

import com.example.demo.Empleados.domain.Empleado;
import com.example.demo.Envios.Envios.domain.Envio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EnvioRepository extends JpaRepository<Envio, Long> {
    List<Envio> findByFechaPreparacionBetween(LocalDate fechaInicio, LocalDate fechaFin);

    List<Envio> findByEmpleado(Empleado empleado);


}
