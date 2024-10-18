package com.example.demo.Computadoras.controller;

import com.example.demo.Computadoras.domain.Computadora;
import com.example.demo.Computadoras.dto.ComputadoraDTO;
import com.example.demo.Computadoras.service.ComputadoraService;
import com.example.demo.Recursos.dto.RecursoDTO;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.StyledEditorKit;
import java.util.List;

@RestController
@CrossOrigin(allowedHeaders = "*")
@RequestMapping("/computadoras")
@AllArgsConstructor

// TODO AGREGAR LOS ENDPOINTS QUE HAGAN FALTA

public class ComputadoraController {

    @Autowired
    public final ComputadoraService computadoraService;

    // Métodos GET //
    @GetMapping("")
    public ResponseEntity<List<ComputadoraDTO>> getAll(){
        List<ComputadoraDTO> computadoras = computadoraService.getAllComputadoras();
        return ResponseEntity.ok(computadoras);
    }

     //URI para que traiga todos los activos por defecto
    @GetMapping("/")
    public ResponseEntity<List<ComputadoraDTO>> getAllByParams(@RequestParam(defaultValue = "true") Boolean esActivo){
        List<ComputadoraDTO> computadoraDTOS = computadoraService.getAllComputadorasByActivo(esActivo);
        return ResponseEntity.ok(computadoraDTOS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComputadoraDTO> getById(@PathVariable Long id){
        try {
            ComputadoraDTO computadora = computadoraService.getComputadoraById(id);
            return ResponseEntity.ok(computadora);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().header("ERROR_MSG", e.getMessage()).build();
        }
    }

    @GetMapping("/masterizados")
    public ResponseEntity<List<ComputadoraDTO>> getAllMasterizados(@RequestParam("esMasterizado") Boolean esMasterizado){
        List<ComputadoraDTO> computadoras = computadoraService.getAllMasterizados(esMasterizado);
        return ResponseEntity.ok(computadoras);
    }

    // Métodos POST para agregar un recurso PD: es autoincremental
    @PostMapping("/")
    public ResponseEntity<ComputadoraDTO> post(@RequestBody ComputadoraDTO computadoraDTO){
        try {
            ComputadoraDTO computadoraDTO1 = computadoraService.create(computadoraDTO);
            return ResponseEntity.ok(computadoraDTO1);
        } catch (BadRequestException e){
            return ResponseEntity.badRequest().header("ERROR_MSG", e.getMessage()).build();
        }
    }

    // Métodos PUT para modificar un recurso de Computadora
    @PutMapping("/{id}")
    public ResponseEntity<ComputadoraDTO> update(
            @PathVariable Long id,
            @RequestBody ComputadoraDTO computadoraDTO) {
        try {
            ComputadoraDTO computadoraActualizada = computadoraService.update(id, computadoraDTO);
            return ResponseEntity.ok(computadoraActualizada);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    //Métodos Logic Detele & Undelete
    @PatchMapping("/desactivar/{id}")
    public ResponseEntity<ComputadoraDTO> deactivateById(@PathVariable Long id){
        try {
            if (computadoraService.logicDelete(id)) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().header("ERROR_MSG", e.getMessage()).build();
        }
    }

    @PatchMapping("/activar/{id}")
    public ResponseEntity<ComputadoraDTO> activateById(@PathVariable Long id){
        try {
            if (computadoraService.logicUndelete(id)) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().header("ERROR_MSG", e.getMessage()).build();
        }
    }

}
