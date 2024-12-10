package com.example.demo.config;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public class SMTPConfig {
    private static SMTPConfig config;
    @Getter
    private String SENDER;

    private SMTPConfig() {
        Dotenv dotenv = Dotenv.configure().directory("./.env").load();
        this.SENDER = dotenv.get("SPRING_MAIL_USERNAME");
    }
    public static SMTPConfig getInstance(){
        if (config == null){
            config = new SMTPConfig();
        }
        return config;
    }
}
