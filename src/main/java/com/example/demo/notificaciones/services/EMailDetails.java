package com.example.demo.notificaciones.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EMailDetails {
    private String recipient;
    private String msgBody;
    private String subject;
    private String attachment;
}
