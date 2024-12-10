package com.example.demo.notificaciones.services;

import com.example.demo.config.SMTPConfig;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
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

    public File cargarFoto(){
        try {
            return new ClassPathResource("icono2.png").getFile();
        } catch (IOException e) {
            throw new RuntimeException(e); //ponemos runtime para que no joda despues
        }
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
    @Override
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
        }
    }
}