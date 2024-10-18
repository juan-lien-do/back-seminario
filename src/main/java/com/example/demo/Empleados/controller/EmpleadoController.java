package com.example.demo.Empleados.controller;


import com.example.demo.Empleados.dto.EmpleadoPostRequestDTO;
import com.example.demo.Empleados.dto.EmpleadoPutRequestDTO;
import com.example.demo.Empleados.dto.EmpleadoResponseDTO;
import com.example.demo.Empleados.service.EmpleadoService;
import com.example.demo.Recursos.dto.RecursoDTO;
import com.example.demo.config.TwilioConfig;
import com.example.demo.exceptions.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/empleados")
@CrossOrigin(allowedHeaders = "*", origins = "http://localhost:5173")
@AllArgsConstructor
public class EmpleadoController {
    @Autowired
    private final EmpleadoService empleadoService;

    @GetMapping("/") // nombre, activo
    public ResponseEntity<List<EmpleadoResponseDTO>> getAll(@RequestParam(required = false) Map<String, String> allParams, HttpServletRequest httpServletRequest) {
        //System.out.println(httpServletRequest.toString());
        /*Enumeration<String> headers = httpServletRequest.getHeaderNames();
        List<String> finalList = Collections.list(headers);
        finalList.stream().forEach((String x)->{
            System.out.println(httpServletRequest.getHeader(x));
        });*/
        //System.out.println(httpServletRequest.getHeader("Authorization"));
        TwilioConfig config = TwilioConfig.getInstance();

        System.out.println();
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
