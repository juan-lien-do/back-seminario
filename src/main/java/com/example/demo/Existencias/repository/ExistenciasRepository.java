package com.example.demo.Existencias.repository;

import com.example.demo.Existencias.domain.Existencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExistenciasRepository extends JpaRepository<Existencia, Long> {
    @Query(value = "SELECT e from Existencia e where e.recurso.id=?1")
    public List<Existencia> findByIdrecurso(Long idRecurso);
}
