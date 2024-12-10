package com.example.demo.notificaciones.services;

import com.example.demo.config.TwilioConfig;
import com.twilio.Twilio;
import com.twilio.converter.Promoter;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.stereotype.Service;

@Service
public class TwilioNotificationService {
    public void notificarUsuario(String phoneNumber, String nuevoEstado, String destinatario){
        TwilioConfig config = TwilioConfig.getInstance();
        Twilio.init(config.getACCOUNT_SID(), config.getAUTH_TOKEN());
        Message message = Message.creator(
                new com.twilio.type.PhoneNumber("whatsapp:"+phoneNumber),
                new com.twilio.type.PhoneNumber("whatsapp:"+config.getSENDER()),
                "Se actualizó el estado del envío: *" + nuevoEstado + "*, para el empleado: *" + destinatario + "* ."
        ).create();

    }
}
