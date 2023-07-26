package org.gaavr.google;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class GoogleSheetsReader {

    private static final String SPREADSHEET_ID = "YOUR_SPREADSHEET_ID"; // Замените на идентификатор вашей таблицы

    public static void main(String[] args) throws IOException {
        // Загрузите учетные данные из файла credentials.json
        GoogleCredential credentials = GoogleCredential.fromStream(new FileInputStream("path/to/credentials.json"))
                .createScoped(Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY));

        // Создайте экземпляр Google Sheets Service
        Sheets sheetsService = new Sheets.Builder(credentials.getTransport(), credentials.getJsonFactory(), credentials)
                .setApplicationName("YOUR_APPLICATION_NAME") // Замените на имя вашего приложения
                .build();

        // Задайте диапазон данных для чтения (например, "Sheet1!A1:C")
        String range = "Sheet1!A1:C";

        // Чтение данных из Google Таблицы
        ValueRange response = sheetsService.spreadsheets().values().get(SPREADSHEET_ID, range).execute();
        List<List<Object>> values = response.getValues();

        // Вывод данных
        if (values == null || values.isEmpty()) {
            System.out.println("No data found.");
        } else {
            for (List<Object> row : values) {
                for (Object cell : row) {
                    System.out.print(cell + "\t");
                }
                System.out.println();
            }
        }
    }
}

