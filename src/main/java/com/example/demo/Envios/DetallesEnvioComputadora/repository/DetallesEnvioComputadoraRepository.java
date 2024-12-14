package com.example.demo.Envios.DetallesEnvioComputadora.repository;

import com.example.demo.Computadoras.domain.Computadora;
import com.example.demo.Envios.DetallesEnvioComputadora.domain.DetalleEnvioComputadora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetallesEnvioComputadoraRepository extends JpaRepository<DetalleEnvioComputadora, Long> {
    List<DetalleEnvioComputadora> findByComputadora(Computadora computadora);
}
