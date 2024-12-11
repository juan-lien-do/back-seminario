package com.example.demo.Envios.Archivos.service;

import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@AllArgsConstructor
@Service

public class ArchivoEnvioService {

    private static final String UPLOADS_DIR = "uploads/";

    public ResponseEntity<String> cargarFoto(@PathVariable Long idEnvio,
                                             @RequestParam MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Foto no encontrado");
            }

            File directorioArchivos = new File(UPLOADS_DIR, String.valueOf(idEnvio));
            if (!directorioArchivos.exists() && !directorioArchivos.mkdirs()) {
                return ResponseEntity.status(500).body("No se pudo crear el directorio");
            }

            byte[] bytes = file.getBytes();
            String base64Encoded = Base64.getEncoder().encodeToString(bytes);
            //Cuanto archivos tiene para saber que nro asignar al próximo
            Integer numMaximo = contarArchivos(directorioArchivos);
            File outputFile = new File(directorioArchivos, "foto_" + (numMaximo+1) + ".txt");
            Files.write(outputFile.toPath(), base64Encoded.getBytes());
            return ResponseEntity.ok().body("Foto almacenada con éxito");

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error al procesar la fotografia");
        }
    }

    public ResponseEntity<List<String>> getFotos(@PathVariable Long idEnvio){
        File directorioUploads = new File(UPLOADS_DIR, String.valueOf(idEnvio));

        if(!directorioUploads.exists() || !directorioUploads.isDirectory()){
            return ResponseEntity.notFound().build();
        }

        File[] archivos = directorioUploads.listFiles();
        if(archivos == null || archivos.length == 0){
            return ResponseEntity.ok(new ArrayList<>());
        }

        List<String> base64List = new ArrayList<>();
        for(File archivo : archivos){
            try {
                String base64 = Files.readString(archivo.toPath());
                base64List.add(base64);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.ok(base64List);
    }

    public Integer contarArchivos(File directorio) {
        if(!directorio.isDirectory()) {
            return -1;
        }
        File[] archivos = directorio.listFiles();
        if(archivos == null || archivos.length == 0){
            return -1;
        }

        int maxNum = -1;
        for(File archivo : archivos){
            String nombreArchivo = archivo.getName();
            if(nombreArchivo.matches("foto_\\d+\\.txt")) {
                String numeroStr = nombreArchivo.replaceAll("\\D", "");
                try {
                    int numero = Integer.parseInt(numeroStr);
                    maxNum = Math.max(maxNum, numero);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        return maxNum;
    }
}
