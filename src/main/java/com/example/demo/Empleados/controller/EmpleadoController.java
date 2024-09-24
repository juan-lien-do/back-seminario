package com.example.demo.Empleados.controller;


import com.example.demo.Empleados.dto.EmpleadoResponseDTO;
import com.example.demo.Empleados.service.EmpleadoService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/empleados")
@CrossOrigin(allowedHeaders = "*")
@AllArgsConstructor
public class EmpleadoController {
    @Autowired
    private final EmpleadoService empleadoService;

    @GetMapping("/")
    public ResponseEntity<List<EmpleadoResponseDTO>> getAll(){
        return ResponseEntity.ok(empleadoService.getAll());
    }
}
