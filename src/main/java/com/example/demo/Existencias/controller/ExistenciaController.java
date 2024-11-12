package com.example.demo.Existencias.controller;


import com.example.demo.Existencias.dto.ExistenciaRequestDTO;
import com.example.demo.Existencias.service.ExistenciasService;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


//@AllArgsConstructor
@RestController
@RequestMapping("/existencias/")
@CrossOrigin(allowedHeaders = "*", origins = "http://localhost:5173")
public class ExistenciaController {
    @Autowired
    private final ExistenciasService existenciasService;

    public ExistenciaController(ExistenciasService existenciasService) {
        this.existenciasService = existenciasService;
    }

    //private final
    @PostMapping("incorporar/")
    public ResponseEntity<String> incorporar(@RequestBody ExistenciaRequestDTO existenciaRequestDTO){
        try {
            existenciasService.incorporarExistencias(existenciaRequestDTO);
            return ResponseEntity.status(201).build();
        } catch (NotFoundException e){
            return ResponseEntity.notFound().header("ERROR", e.getMessage()).build();
        }
    }

    @PostMapping("disminuir/")
    public ResponseEntity<Long> disminuir(@RequestBody ExistenciaRequestDTO existenciaRequestDTO){
        try {
            Long rta = existenciasService.disminuirExistencias(existenciaRequestDTO);
            return ResponseEntity.status(201).body(rta);
        } catch (NotFoundException e){
            return ResponseEntity.notFound().header("ERROR", e.getMessage()).build();
        } catch (BadRequestException e){
            return ResponseEntity.badRequest().header("ERROR", e.getMessage()).build();
        }
    }



}
