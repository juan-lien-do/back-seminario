package com.example.demo.notificaciones.services;


import com.example.demo.config.SMTPConfig;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EMailServiceImpl implements EMailService{
    @Autowired
    private final JavaMailSender javaMailSender;


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
            return "xd";
        }
    }

    @Override
    public String enviarMailConAdjunto(EMailDetails eMailDetails) {
        return "no implementado jijo";
    }
}
