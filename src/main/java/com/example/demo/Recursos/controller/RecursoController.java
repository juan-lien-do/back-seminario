package com.example.demo.Recursos.controller;

import com.example.demo.Recursos.dto.RecursoDTO;
import com.example.demo.Recursos.service.RecursoService;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(allowedHeaders = "*")
@RequestMapping("/recursos")
@AllArgsConstructor

public class RecursoController {

    @Autowired
    public final RecursoService recursoService;

    @GetMapping("")
    public ResponseEntity<List<RecursoDTO>> getAll(){
        List<RecursoDTO> muebles = recursoService.getAllRecursos();
        return ResponseEntity.ok(muebles);
    }

    @PostMapping("/")
    public ResponseEntity<RecursoDTO> post(@RequestBody RecursoDTO recursoDTO){
        try {
            RecursoDTO recursoDTO1 = recursoService.create(recursoDTO);
            return ResponseEntity.ok(recursoDTO1);
        } catch (BadRequestException e){
            return ResponseEntity.badRequest().header("ERROR_MSG", e.getMessage()).build();
        }
    }
}
