package com.example.demo.notificaciones.services;

import com.example.demo.config.SMTPConfig;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
@AllArgsConstructor
public class EMailServiceImpl implements EMailService{

    @Autowired
    private final JavaMailSender javaMailSender;

    public File cargarFoto() throws IOException {
        // Ruta original de la imagen
        File originalImage = new ClassPathResource("icono2.png").getFile();

        // Ruta temporal para guardar la imagen redimensionada
        File resizedImage = new File("src/main/resources/iconoReducido2.png");

        // Redimensionar la imagen a un tamaño más pequeño
        Thumbnails.of(originalImage)
                .size(50, 75) // Redimension
                .outputQuality(0.8) // Calidad de compresión (80%)
                .toFile(resizedImage);

        return resizedImage;
    }

    @Override
    public String enviarMailSencillo(EMailDetails eMailDetails) {
        try{
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            SMTPConfig config = SMTPConfig.getInstance();
            mailMessage.setFrom(config.getSENDER());
            mailMessage.setTo(eMailDetails.getRecipient());
            mailMessage.setText(eMailDetails.getMsgBody());
            mailMessage.setSubject(eMailDetails.getSubject());
            javaMailSender.send(mailMessage);
            return "Salio bien";
        } catch (Exception e){
            return "Error?" + e.getMessage();
        }
    }
    /* @Override
    public String enviarMailConAdjunto(EMailDetails eMailDetails) {


        MimeMessage mimeMessage
                = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try {
            SMTPConfig config = SMTPConfig.getInstance();

            // Setting multipart as true for attachments to
            // be send
            mimeMessageHelper
                    = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(config.getSENDER());
            mimeMessageHelper.setTo(eMailDetails.getRecipient());
            mimeMessageHelper.setText(eMailDetails.getMsgBody());
            mimeMessageHelper.setSubject(
                    eMailDetails.getSubject());

            // Adding the attachment
            FileSystemResource file
                    = new FileSystemResource(cargarFoto());

            mimeMessageHelper.addAttachment(
                    file.getFilename(), file);

            // Sending the mail
            javaMailSender.send(mimeMessage);
            return "Mail sent Successfully";
        }

        // Catch block to handle MessagingException
        catch (MessagingException e) {

            // Display message when exception occurred
            return "Error while sending mail!!!";
        } */

        @Override
        public String enviarMailConAdjunto(EMailDetails eMailDetails) {

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper;

            try {
                SMTPConfig config = SMTPConfig.getInstance();

                // Configurar el MimeMessageHelper con multipart
                mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
                mimeMessageHelper.setFrom(config.getSENDER());
                mimeMessageHelper.setTo(eMailDetails.getRecipient());
                mimeMessageHelper.setSubject(eMailDetails.getSubject());

                // Ruta de la imagen
                FileSystemResource file = new FileSystemResource(cargarFoto());
                String contentId = "inlineImage";

                // Crear el contenido HTML con la imagen en el cuerpo
                String htmlContent = "<html><body>"
                        + "<h3>" + eMailDetails.getMsgBody().replace("\n", "<br>") + "</h3>"
                        + "<div style='display: flex; align-items: center;'>"
                        + "    <img src='cid:" + contentId + "' alt='imagen' style='margin-right: 10px;' />"
                        + "    <span style='font-size: 14px; color: gray;'>Administración de ALMACÉN IT</span>"
                        + "</div>"
                        + "</body></html>";

                mimeMessageHelper.setText(htmlContent, true); // Indica que el contenido es HTML

                // Agregar la imagen como contenido inline
                mimeMessageHelper.addInline(contentId, file);

                // Enviar el correo
                javaMailSender.send(mimeMessage);
                return "Mail sent Successfully";

            } catch (MessagingException e) {
                // Manejar errores
                e.printStackTrace();
                return "Error while sending mail!!!";
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
}
