package com.example.demo.Envios.QR.controller;

import com.example.demo.Envios.QR.service.QRService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("envios/qrCode")
public class QRController {

    private final QRService qrService;

    public QRController(QRService qrService) {
        this.qrService = qrService;
    }

    @PostMapping("/generateQR")
    public ResponseEntity<byte[]> generateQR(
            @RequestParam("texto") String texto,
            @RequestParam("size") int size) {
        try {
            byte[] qrImage = qrService.generateQRCode(texto, size);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "image/png");
            headers.set("Content-Disposition", "attachment; filename=qr.png");
            return new ResponseEntity<>(qrImage, headers, HttpStatus.OK); // Retornar la imagen PNG
        } catch (Exception e) {
            e.printStackTrace(); // Manejar excepciones
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // Retornar error si algo falla
        }
    }
}

