package org.gaavr.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
public class GoogleSheetsReaderService {

    private static final String SPREADSHEET_ID = "YOUR_SPREADSHEET_ID";
    private static final String APPLICATION_NAME = "YOUR_APPLICATION_NAME";
    private final Resource credentialsResource;

    public GoogleSheetsReaderService(@Value("${google.credentials.path}") Resource credentialsResource) {
        this.credentialsResource = credentialsResource;
    }

    public List<List<Object>> readDataFromGoogleSheets(String range) throws IOException {
        // Загрузите учетные данные из файла credentials.json
        GoogleCredential credentials = GoogleCredential.fromStream(credentialsResource.getInputStream())
                .createScoped(Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY));

        // Создайте экземпляр Google Sheets Service
        Sheets sheetsService = new Sheets.Builder(credentials.getTransport(), credentials.getJsonFactory(), credentials)
                .setApplicationName(APPLICATION_NAME)
                .build();

        // Чтение данных из Google Таблицы
        ValueRange response = sheetsService.spreadsheets().values().get(SPREADSHEET_ID, range).execute();
        return response.getValues();
    }
}


