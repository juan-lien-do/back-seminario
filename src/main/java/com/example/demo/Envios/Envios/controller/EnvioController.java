package com.example.demo.Envios.Envios.controller;

import com.example.demo.Computadoras.domain.Computadora;
import com.example.demo.Computadoras.repository.ComputadoraRepository;
import com.example.demo.Computadoras.service.ComputadoraService;
import com.example.demo.Envios.DetallesEnvioComputadora.domain.DetalleEnvioComputadora;
import com.example.demo.Envios.DetallesEnvioComputadora.repository.DetallesEnvioComputadoraRepository;
import com.example.demo.Envios.DetallesEnvioRecurso.domain.DetalleEnvioRecurso;
import com.example.demo.Envios.DetallesEnvioRecurso.repository.DetalleEnvioRecursoRepository;
import com.example.demo.Envios.Envios.dto.EnvioPostDTO;
import com.example.demo.Envios.Envios.dto.EnvioResponseDTO;
import com.example.demo.Envios.Envios.service.EnvioService;
import com.example.demo.Existencias.domain.Existencia;
import com.example.demo.Existencias.repository.ExistenciasRepository;
import com.example.demo.Existencias.service.ExistenciasService;
import com.example.demo.Recursos.domain.Recurso;
import com.example.demo.Recursos.dto.RecursoDTO;
import com.example.demo.Recursos.service.RecursoService;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(allowedHeaders = "*")
@RequestMapping("/envios")
@AllArgsConstructor

public class EnvioController {

    @Autowired
    public final EnvioService envioService;
    @Autowired
    private ComputadoraService computadoraService;
    @Autowired
    private ComputadoraRepository computadoraRepository;
    @Autowired
    private DetallesEnvioComputadoraRepository detallesEnvioComputadoraRepository;
    @Autowired
    private DetalleEnvioRecursoRepository detalleEnvioRecursoRepository;
    @Autowired
    private ExistenciasRepository existenciasRepository;
    @Autowired
    private RecursoService recursoService;
    @Autowired
    private ExistenciasService existenciasService;

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

    //  TODO seguir creando función
    @PutMapping("/devolver-Recurso/{idDetalle}")
    public ResponseEntity<String> devolverRecurso(@PathVariable Long idDetalle){
        try{
            Optional<DetalleEnvioRecurso> detRecurso = detalleEnvioRecursoRepository.findById(idDetalle);
            if (detRecurso.isPresent()){
                DetalleEnvioRecurso detRec = detRecurso.get();
                recursoService.actualizarEsDevuelto(detRec.getIdDetalleRecurso(), true);
                Existencia existencia = detRecurso.get().getExistencia();
                existenciasService.aumentarExistencias(existencia.getId(), detRecurso.get().getCantidad());
            } else {
                throw new NotFoundException("Detalle envio recurso no encontrado");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().header("ERROR", e.getMessage()).build();
        }
        return ResponseEntity.status(201).build();
    }

    //  TODO seguir creando función
    @PutMapping("/devolver-Computadora/{idDetalle}")
    public ResponseEntity<String> devolverComputadora(@PathVariable Long idDetalle){
        try {
            Optional<DetalleEnvioComputadora> detComp = detallesEnvioComputadoraRepository.findById(idDetalle);
            if (detComp.isPresent()) {
                Computadora computadora = detComp.get().getComputadora();
                computadoraService.actualizarEnUso(computadora.getIdComputadora(), false);
                return ResponseEntity.status(201).build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Detalle no encontrado");
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().header("ERROR", e.getMessage()).build();
        }
    }
}
