package com.example.demo.Recursos.controller;

import com.example.demo.Recursos.dto.RecursoDTO;
import com.example.demo.Recursos.service.RecursoService;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
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

    @GetMapping("/")
    public ResponseEntity<List<RecursoDTO>> getAllByParams(@RequestParam(required = true) Boolean activo){
        List<RecursoDTO> recursoDTOS = recursoService.getAllRecursosByActivo(activo);
        return ResponseEntity.ok(recursoDTOS);
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
    // voy a usar un patch para que se enoje ricky
    /*
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
    }*/

    @PatchMapping("/desactivar/{id}")
    public ResponseEntity<String> deactivateById(@PathVariable Long id){
        try {
            if (recursoService.logicDelete(id)) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().header("ERROR_MSG", e.getMessage()).build();
        } catch (BadRequestException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PatchMapping("/activar/{id}")
    public ResponseEntity<RecursoDTO> activateById(@PathVariable Long id){
        try {
            if (recursoService.logicUndelete(id)) {
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