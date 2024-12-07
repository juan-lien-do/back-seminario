package com.example.demo.config;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;

public class SMTPConfig {
    private static SMTPConfig config;
    @Getter
    private String SENDER;
    private SMTPConfig() {
        Dotenv dotenv = Dotenv.configure().directory("./.env").load();

        this.SENDER = dotenv.get("EMAIL_SENDER");
    }

    public static SMTPConfig getInstance(){
        if (config == null){
            config = new SMTPConfig();
        }
        return config;
    }
}
