package com.example.demo.Envios.Archivos.controller;

import com.example.demo.Envios.Archivos.FotoDTO.FotoDTO;
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

    //Cargar foto devolucion
    @PostMapping("/cargar_fotoDev/{idEnvio}")
    public ResponseEntity<String> cargarFotoDev(@PathVariable Long idEnvio,
                                             @RequestParam("file") MultipartFile file) {
        try {
            return archivoService.cargarFoto(idEnvio, file, "devolucion");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    //Cargar foto envio
    @PostMapping("/cargar_fotoEnv/{idEnvio}")
    public ResponseEntity<String> cargarFotoEnv(@PathVariable Long idEnvio,
                                             @RequestParam("file") MultipartFile file) {
        try {
            return archivoService.cargarFoto(idEnvio, file, "envio");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/fotos-envio/{idEnvio}")
    public ResponseEntity<List<FotoDTO>> getFotoEnv(@PathVariable Long idEnvio){
        return archivoService.getFotos(idEnvio, "envio");
    }

    @GetMapping("/fotos-devol/{idEnvio}")
    public ResponseEntity<List<FotoDTO>> getFotoDev(@PathVariable Long idEnvio){
        return archivoService.getFotos(idEnvio, "devolucion");
    }

    @DeleteMapping("/fotos/eliminar/{idEnvio}/{nombreFoto}")
    public ResponseEntity<String> eliminarFoto(@PathVariable Long idEnvio,
                                               @PathVariable String nombreFoto){
        try {
            return archivoService.eliminarFoto(idEnvio, nombreFoto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
