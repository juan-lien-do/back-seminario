package com.example.demo.Envios.Envios.controller;

import com.example.demo.Envios.Envios.dto.EnvioPostDTO;
import com.example.demo.Envios.Envios.dto.EnvioResponseDTO;
import com.example.demo.Envios.Envios.service.EnvioService;
import com.example.demo.Recursos.dto.RecursoDTO;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(allowedHeaders = "*")
@RequestMapping("/envios")
@AllArgsConstructor

public class EnvioController {

    @Autowired
    public final EnvioService envioService;

    // ------------------------------------- MÉTODOS GET -----------------------------------------------//

    @GetMapping("")
    public ResponseEntity<List<EnvioResponseDTO>> getAll(){
        List<EnvioResponseDTO> envios = envioService.getAllEnvios();
        return ResponseEntity.ok(envios);
    }

    // ------------------------------------- MÉTODOS POST -----------------------------------------------//

    @PostMapping("/")
    public ResponseEntity<EnvioResponseDTO> create(@RequestBody EnvioPostDTO envioPostDTO){
        try {
            return ResponseEntity.status(201).body(envioService.create(envioPostDTO));
        } catch (NotFoundException e){
            return ResponseEntity.notFound().header("ERROR", e.getMessage()).build();
        } catch (BadRequestException e){
            return ResponseEntity.badRequest().header("ERROR", e.getMessage()).build();
        }

    }

    @PutMapping("/{idEnvio}/{idEstado}")
    public ResponseEntity<String> asdasasd(@PathVariable Long idEnvio, @PathVariable Long idEstado){
        try {
            envioService.cambiarEstado(idEnvio, idEstado);
            return ResponseEntity.status(201).build();
        } catch (Exception e){
            return ResponseEntity.notFound().header("ERROR", e.getMessage()).build();
        }
    }

}
