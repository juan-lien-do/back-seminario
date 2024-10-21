package com.example.demo.Empleados.repository;

import com.example.demo.Empleados.domain.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
    List<Empleado> findByNombreAndActivo(String nombre, Boolean activo);
    List<Empleado> findByNombreContaining(String nombre);
    List<Empleado> findByNombreContainingAndActivo(String nombre, Boolean activo);
    List<Empleado> findByCuil(Long cuil);

}
