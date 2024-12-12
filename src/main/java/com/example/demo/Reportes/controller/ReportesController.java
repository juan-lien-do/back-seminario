package com.example.demo.Reportes.controller;

import com.example.demo.Reportes.dto.ReporteCompletoDTO;
import com.example.demo.Reportes.service.ReportesService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Date;

@RestController
@RequestMapping("/reportes/")
@AllArgsConstructor
public class ReportesController {
    @Autowired
    private final ReportesService reportesService;

    // NO ANDA TODAVÍA
    @GetMapping("")
    public ResponseEntity<ReporteCompletoDTO> conseguirReporteCompleto(
            @RequestParam(value = "fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(value = "fechaFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin){
        try {
            return ResponseEntity.ok(reportesService.conseguirReporteCompleto(fechaInicio, fechaFin));
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }

    }
}
