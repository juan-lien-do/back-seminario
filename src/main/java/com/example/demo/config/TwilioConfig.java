package com.example.demo.config;


import com.twilio.Twilio;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;

// Singleton pattern
public class TwilioConfig {
    private static TwilioConfig twilioConfig;
    @Getter
    private final String ACCOUNT_SID;
    @Getter
    private final String AUTH_TOKEN;
    @Getter
    private final String SENDER;


    private TwilioConfig() {
        Dotenv dotenv = Dotenv.configure().directory("./.env").load();

        this.ACCOUNT_SID = dotenv.get("ACCOUNT_SID");
        this.AUTH_TOKEN = dotenv.get("AUTH_TOKEN");
        this.SENDER = dotenv.get("SENDER");
        System.out.println(SENDER);
    }


    public static TwilioConfig getInstance() {
        if (twilioConfig == null) {
            twilioConfig = new TwilioConfig();
        }
        return twilioConfig;
    }
}

