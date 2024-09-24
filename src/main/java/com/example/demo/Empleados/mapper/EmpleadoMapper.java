package com.example.demo.Empleados.mapper;

import com.example.demo.Empleados.domain.Empleado;
import com.example.demo.Empleados.dto.EmpleadoResponseDTO;

public class EmpleadoMapper {


    //public static Empleado toEntity();
    public static EmpleadoResponseDTO toResponseDTO(Empleado empleado){
        return EmpleadoResponseDTO.builder()
                .idEmpleado(empleado.getIdEmpleado())
                .cuil(empleado.getCuil())
                .nombre(empleado.getNombre())
                .activo(empleado.getActivo())
                .mail(empleado.getMail())
                .telefono(empleado.getTelefono())
                .build();
    }
}
