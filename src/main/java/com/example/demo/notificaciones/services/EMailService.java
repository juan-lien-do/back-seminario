package com.example.demo.notificaciones.services;

public interface EMailService {
    String enviarMailSencillo(EMailDetails eMailDetails);
    String enviarMailConAdjunto(EMailDetails eMailDetails);
}
