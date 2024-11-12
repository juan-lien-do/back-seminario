package com.example.demo.Solicitudes.controller;

import com.example.demo.Solicitudes.dtos.SolicitudDTOGet;
import com.example.demo.Solicitudes.dtos.SolicitudDTOPost;
import com.example.demo.Solicitudes.service.SolicitudService;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/solicitudes")
@AllArgsConstructor
public class SolicitudController {
    @Autowired
    private final SolicitudService solicitudService;

    @GetMapping("/")
    public ResponseEntity<List<SolicitudDTOGet>> getAll() {
        return ResponseEntity.ok(solicitudService.getAll());
    }


    @PostMapping("/")
    public ResponseEntity<Long> create(@RequestBody SolicitudDTOPost solicitudDTOPost) {
        try {
            return ResponseEntity.status(201).body(solicitudService.create(solicitudDTOPost));
        } catch (NotFoundException notFoundException) {
            return ResponseEntity.notFound().header("ERROR", notFoundException.getMessage()).build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Long> incorporar(@PathVariable Long id) {
        try {
            return ResponseEntity.status(201).body(solicitudService.incorporar(id));
        } catch (NotFoundException notFoundException) {
            return ResponseEntity.notFound().header("ERROR", notFoundException.getMessage()).build();
        } catch (BadRequestException notFoundException) {
            return ResponseEntity.badRequest().header("ERROR", notFoundException.getMessage()).build();
        }
    }
}
