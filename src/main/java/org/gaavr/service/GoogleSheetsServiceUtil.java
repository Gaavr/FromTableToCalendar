package org.gaavr.google;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

@Component
public class GoogleSheetsServiceUtil {

    public Sheets getSheetsService(String credentialsPath) throws IOException {
        GoogleCredential credentials;
        try (InputStream inputStream = getClass().getResourceAsStream(credentialsPath)) {
            credentials = GoogleCredential
                    .fromStream(inputStream)
                    .createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS));
        }

        return new Sheets.Builder(credentials.getTransport(), credentials.getJsonFactory(), credentials)
                .setApplicationName("YOUR_APPLICATION_NAME")
                .build();
    }
}
