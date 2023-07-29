package org.gaavr.google;

import lombok.AllArgsConstructor;
import org.gaavr.config.GoogleConfig;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;

@Component
@AllArgsConstructor
public class GoogleSheetsReader {

    private final GoogleConfig googleConfig;

    public List<List<Object>> readData(String spreadsheetId, String range) {
        try {
            GoogleCredential credential = authorize();
            Sheets sheetsService = new Sheets.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance(),
                    credential)
                    .setApplicationName(googleConfig.getApplicationName())
                    .build();

            return sheetsService.spreadsheets().values()
                    .get(spreadsheetId, range)
                    .execute()
                    .getValues();
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
            return null;
        }
    }

    private GoogleCredential authorize() {
        try {
            GoogleCredential credentials;
            try (InputStream inputStream = getClass().getResourceAsStream(googleConfig.getCredentialsPath())) {
                credentials = GoogleCredential
                        .fromStream(inputStream)
                        .createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS));
            }

            return credentials;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
