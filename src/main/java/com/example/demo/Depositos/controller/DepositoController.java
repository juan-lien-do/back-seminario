package com.example.demo.Depositos.controller;

import com.example.demo.Depositos.dto.DepositoDTO;
import com.example.demo.Depositos.serivce.DepositoService;
import com.example.demo.Recursos.dto.RecursoDTO;
import com.example.demo.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(allowedHeaders = "*")
@RequestMapping("/depositos")
@AllArgsConstructor

public class DepositoController {

    @Autowired
    public final DepositoService depositoService;

    @GetMapping("/{id}")
    public ResponseEntity<DepositoDTO> getById(@PathVariable Long id){
        try {
            DepositoDTO deposito = depositoService.getDepositoById(id);
            return ResponseEntity.ok(deposito);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().header("ERROR_MSG", e.getMessage()).build();
        }
    }
}
