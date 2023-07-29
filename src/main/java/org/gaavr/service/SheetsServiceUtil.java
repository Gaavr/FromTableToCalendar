package org.gaavr.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import lombok.RequiredArgsConstructor;
import org.gaavr.config.GoogleConfig;
import org.gaavr.google.GoogleAuthorizeUtil;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Component
@RequiredArgsConstructor
public class SheetsServiceUtil {

    private final GoogleConfig googleConfig;
    private final GoogleAuthorizeUtil googleAuthorizeUtil;

    public Sheets getSheetsService() {
        try {
            GoogleCredential credential = googleAuthorizeUtil.authorize();
            return new Sheets.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance(), credential)
                    .setApplicationName(googleConfig.getApplicationName())
                    .build();
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
            System.out.println("!!!!!!!!!!!!!!!");
            System.out.println("Тут упали");
            System.out.println("!!!!!!!!!!!!!!!");
            return null;
        }
    }
}
