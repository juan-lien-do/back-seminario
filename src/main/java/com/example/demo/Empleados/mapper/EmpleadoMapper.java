package com.example.demo.Empleados.mapper;

import com.example.demo.Empleados.domain.Empleado;
import com.example.demo.Empleados.dto.EmpleadoPostRequestDTO;
import com.example.demo.Empleados.dto.EmpleadoPutRequestDTO;
import com.example.demo.Empleados.dto.EmpleadoResponseDTO;

public class EmpleadoMapper {

    public static Empleado toEntity(EmpleadoPutRequestDTO empleado){
        return Empleado.builder()
                .idEmpleado(empleado.getIdEmpleado())
                .cuil(empleado.getCuil())
                .nombre(empleado.getNombre())
                .mail(empleado.getMail())
                .ws(empleado.getWs())
                .telefono(empleado.getTelefono())
                .activo(empleado.getActivo())
                .build();
    }

    public static Empleado toEntity(EmpleadoPostRequestDTO empleado){
        return Empleado.builder()
                .cuil(empleado.getCuil())
                .nombre(empleado.getNombre())
                .activo(true)
                .mail(empleado.getMail())
                .ws(empleado.getWs())
                .telefono(empleado.getTelefono())
                .build();
    }
    public static EmpleadoResponseDTO toResponseDTO(Empleado empleado){
        return EmpleadoResponseDTO.builder()
                .idEmpleado(empleado.getIdEmpleado())
                .cuil(empleado.getCuil())
                .nombre(empleado.getNombre())
                .activo(empleado.getActivo())
                .mail(empleado.getMail())
                .ws(empleado.getWs())
                .telefono(empleado.getTelefono())
                .deleteDate(empleado.getDeleteDate())
                .build();
    }
}
