package com.example.demo.Computadoras.repository;

import com.example.demo.Computadoras.domain.Computadora;
import com.example.demo.Recursos.domain.Recurso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComputadoraRepository extends JpaRepository<Computadora, Long> {
    List<Computadora> findByEsActivo(Boolean esActivo);
    List<Computadora> findByEsMasterizado(Boolean esActivo);
    List<Computadora> findByIdTipo(Long idTipo);
    Boolean existsByNroWs(Long nroWs);
}
