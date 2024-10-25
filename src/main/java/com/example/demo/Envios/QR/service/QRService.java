package com.example.demo.Envios.QR.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

@Service
public class QRService {
    public byte[] generateQRCode(String idEnvio, int size) throws Exception {
        //Modificar esta URL
        String url = "https://youtu.be/dQw4w9WgXcQ?si=w2SXsrrHIYIk6RFz"; //agrega +idEnvio
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, size, size);

        saveQRCodeImage(bitMatrix, "qr_image" + idEnvio +".png");

        BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", outputStream);
        return outputStream.toByteArray();
    }

    private void saveQRCodeImage(BitMatrix bitMatrix, String fileName) throws IOException {
        BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
        File outputFile = new File("cambiarPath" + fileName); // Ver que onda esta ruta
        ImageIO.write(image, "png", outputFile);
        System.out.println("QR Code saved at: " + outputFile.getAbsolutePath());
    }
}

