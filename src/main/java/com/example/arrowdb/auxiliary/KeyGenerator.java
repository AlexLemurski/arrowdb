package com.example.arrowdb.auxiliary;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class KeyGenerator {

    public static String generateKey(String name) {
        return DigestUtils.md5Hex(name + LocalDateTime.now());
    }
}
