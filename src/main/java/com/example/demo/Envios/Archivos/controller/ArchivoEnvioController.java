package com.example.demo.Envios.Archivos.controller;

import com.example.demo.Envios.Archivos.service.ArchivoEnvioService;
import com.example.demo.Envios.Envios.service.EnvioService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;

@RestController
@CrossOrigin(allowedHeaders = "*")
@RequestMapping("/archivos")
@AllArgsConstructor

public class ArchivoEnvioController {

    @Autowired
    private ArchivoEnvioService archivoService;

    private static final String UPLOADS_DIR = "uploads/";

    @PostMapping("/cargar_foto/{idEnvio}")
    public ResponseEntity<String> cargarFoto(@PathVariable Long idEnvio,
                                             @RequestParam("file") MultipartFile file) {
        try {
            return archivoService.cargarFoto(idEnvio, file);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/fotos/{idEnvio}")
    public ResponseEntity<List<String>> getFoto(@PathVariable Long idEnvio){
        return archivoService.getFotos(idEnvio);
    }
}
