package com.example.arrowdb.auxiliary;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class PassGenerator {

    public static String randomPass(){
        String symbols = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890!@$%^&*";
        SecureRandom rnd = new SecureRandom();
        int len = 12;
        StringBuilder pass = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            pass.append(symbols.charAt(rnd.nextInt(symbols.length())));
        }
        return pass.toString();
    }
}