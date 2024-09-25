package com.example.demo.Recursos.controller;

import com.example.demo.Recursos.dto.RecursoDTO;
import com.example.demo.Recursos.service.RecursoService;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    /* Métodos GET */
        //Todos los
    @GetMapping("")
    public ResponseEntity<List<RecursoDTO>> getAll(){
        List<RecursoDTO> recursos = recursoService.getAllRecursos();
        return ResponseEntity.ok(recursos);
    }

    @GetMapping("/filtrar")
    public ResponseEntity<List<RecursoDTO>> getAllbyCategoria(
            @RequestParam("categoria") Long categoria){
        List<RecursoDTO> recursos = recursoService.getAllRecursosCat(categoria);
        return ResponseEntity.ok(recursos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecursoDTO> getById(@PathVariable Long id){
        try {
            RecursoDTO recurso = recursoService.getRecursoById(id);
            return ResponseEntity.ok(recurso);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().header("ERROR_MSG", e.getMessage()).build();
        }
    }


    // Métodos POST para agregar un recurso PD: es autoincremental
    @PostMapping("/")
    public ResponseEntity<RecursoDTO> post(@RequestBody RecursoDTO recursoDTO){
        try {
            RecursoDTO recursoDTO1 = recursoService.create(recursoDTO);
            return ResponseEntity.ok(recursoDTO1);
        } catch (BadRequestException e){
            return ResponseEntity.badRequest().header("ERROR_MSG", e.getMessage()).build();
        }
    }

    // Métodos DELETE para eliminar un objeto particular por ID de la BBDD
    @DeleteMapping("/{id}")
    public ResponseEntity<RecursoDTO> deleteById(@PathVariable Long id) {
        try {
            if (recursoService.logicDelete(id)) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().header("ERROR_MSG", e.getMessage()).build();
        }
    }

    //Métodos PUT para modificar objetos de la BBDD para recursos
    @PutMapping("/{id}")
    public ResponseEntity<RecursoDTO> update(
            @PathVariable Long id,
            @RequestBody RecursoDTO recursoDTO) {
        try {
            RecursoDTO recursoActualizado = recursoService.update(id, recursoDTO);
            return ResponseEntity.ok(recursoActualizado);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}