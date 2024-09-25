package com.example.demo.Empleados.controller;


import com.example.demo.Empleados.dto.EmpleadoPostRequestDTO;
import com.example.demo.Empleados.dto.EmpleadoPutRequestDTO;
import com.example.demo.Empleados.dto.EmpleadoResponseDTO;
import com.example.demo.Empleados.service.EmpleadoService;
import com.example.demo.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/empleados")
@CrossOrigin(allowedHeaders = "*")
@AllArgsConstructor
public class EmpleadoController {
    @Autowired
    private final EmpleadoService empleadoService;

    @GetMapping("/") // nombre, activo
    public ResponseEntity<List<EmpleadoResponseDTO>> getAll(@RequestParam(required = false) Map<String, String> allParams) {
        return ResponseEntity.ok(empleadoService.getAll(allParams));
    }

    @PostMapping("/")
    public ResponseEntity<String> postEmpleado(@RequestBody @Validated EmpleadoPostRequestDTO empleadoPostRequestDTO) {
        try {
            return ResponseEntity.status(201).body(empleadoService.saveEmpleado(empleadoPostRequestDTO));
        } catch (Exception e) {
            System.out.println(e.toString());
            return ResponseEntity.badRequest().header("xd", e.getMessage()).build();
        }
    }

    @PutMapping("/")
    public ResponseEntity<String> putEmpleado(@RequestBody @Validated EmpleadoPutRequestDTO empleadoPutRequestDTO) {
        try {
            return ResponseEntity.status(201).body(empleadoService.updateEmpleado(empleadoPutRequestDTO));
        } catch (NotFoundException e){
            return ResponseEntity.notFound().header("error",e.getMessage()).build();
        }
    }

    @PatchMapping("/activar/{id}")
    public ResponseEntity<String> activarEmpleado(@PathVariable Long id){
        try{
            return ResponseEntity.status(201).body(empleadoService.activarEmpleado(id));
        } catch (NotFoundException e){
            return ResponseEntity.notFound().header("error",e.getMessage()).build();
        }
    }

    @PatchMapping("/desactivar/{id}")
    public ResponseEntity<String> desactivarEmpleado(@PathVariable Long id){
        try{
            return ResponseEntity.status(201).body(empleadoService.desactivarEmpleado(id));
        } catch (NotFoundException e){
            return ResponseEntity.notFound().header("error",e.getMessage()).build();
        }
    }

}
