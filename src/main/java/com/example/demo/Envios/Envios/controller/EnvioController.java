package com.example.demo.Envios.Envios.controller;

import com.example.demo.Envios.Envios.domain.Envio;
import com.example.demo.Envios.Envios.dto.EnvioDTO;
import com.example.demo.Envios.Envios.service.EnvioService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<List<EnvioDTO>> getAll(){
        List<EnvioDTO> envios = envioService.getAllEnvios();
        return ResponseEntity.ok(envios);
    }

}
