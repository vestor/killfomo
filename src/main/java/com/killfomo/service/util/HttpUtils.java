package com.killfomo.service.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Base64;

/**
 * Created by manishs on 16/05/18.
 */
public class HttpUtils {


    public static HttpHeaders getHttpHeadersForBasic(String apiKey) {
        String plainCreds = apiKey + ":X";
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.getEncoder().encode(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + base64Creds);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
