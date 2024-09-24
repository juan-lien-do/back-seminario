package com.example.demo.Empleados.service;

import com.example.demo.Empleados.dto.EmpleadoResponseDTO;
import com.example.demo.Empleados.mapper.EmpleadoMapper;
import com.example.demo.Empleados.repository.EmpleadoRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EmpleadoService {
    @Autowired
    private final EmpleadoRepository empleadoRepository;


    public List<EmpleadoResponseDTO> getAll(){
        return empleadoRepository.findAll().stream().map(EmpleadoMapper::toResponseDTO).toList();
    }
}
