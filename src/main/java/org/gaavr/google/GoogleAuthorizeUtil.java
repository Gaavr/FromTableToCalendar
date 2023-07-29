package org.gaavr.google;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.services.sheets.v4.SheetsScopes;
import lombok.RequiredArgsConstructor;
import org.gaavr.config.GoogleConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class GoogleAuthorizeUtil {

    private final GoogleConfig googleConfig;

    public GoogleCredential authorize() {
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