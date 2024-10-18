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

/*
import com.twilio.Twilio;
import com.twilio.converter.Promoter;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.net.URI;
import java.math.BigDecimal;

public class Example {
  // Find your Account Sid and Token at twilio.com/console
  public static final String ACCOUNT_SID = "ACd5c5c82e519d884b1a38d8728a3c9b54";
  public static final String AUTH_TOKEN = "[AuthToken]";

  public static void main(String[] args) {
    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    Message message = Message.creator(
      new com.twilio.type.PhoneNumber("whatsapp:+5493525413678"),
      new com.twilio.type.PhoneNumber("whatsapp:+14155238886"),
      "HX229f5a04fd0510ce1b071852155d3e75",
      "{"1":"hola esto es un test"}",
      "Your message")
    .create();

    System.out.println(message.getSid());
  }
}
 */