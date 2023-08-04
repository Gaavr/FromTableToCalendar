package org.gaavr.service;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import lombok.RequiredArgsConstructor;
import org.gaavr.config.GoogleConfig;
import org.gaavr.util.GoogleAuthorizeUtil;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GoogleSheetsReaderService {

    private final GoogleAuthorizeUtil googleAuthorizeUtil;
    private final GoogleConfig googleConfig;

    //example
    public List<List<Object>> readData(String spreadsheetId, String range) {
        try {
            Sheets sheetsService = googleAuthorizeUtil.getSheetsService();

            ValueRange response = sheetsService.spreadsheets().values()
                    .get(spreadsheetId, range)
                    .execute();

            return response.getValues();
        } catch (IOException | GeneralSecurityException e) {
            System.out.println("Error getting data from table " + e.getCause());
        }
        return null;
    }

    public List<List<Object>> getListOfEvents() throws IOException, GeneralSecurityException {
        String spreadsheetId = googleConfig.getSpreadsheetId();
        String sheetName = "УБВТ21";  // Используем только имя листа
        Sheets service = googleAuthorizeUtil.getSheetsService();

        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, sheetName)
                .execute();
        List<List<Object>> values = response.getValues();

        List<List<Object>> result = new ArrayList<>();

        if (values == null || values.isEmpty()) {
            System.out.println("No data found.");
        } else {
            // Регулярные выражения для валидации данных
            String datePattern = "\\d{2}\\.\\d{2}"; // Пример шаблона для даты
            String timePattern = "\\d{2}\\.\\d{2}-\\d{2}\\.\\d{2}"; // Пример шаблона для времени
            String subjectPattern = "\\D+"; // Пример шаблона для названия предмета
            String namePattern = "\\D+"; // Пример шаблона для имени

            for (List<Object> row : values) {
                if (row.size() < 3) continue;
                List<Object> event = new ArrayList<>();

                // Валидация данных с использованием регулярных выражений
                if (row.get(0).toString().matches(datePattern)) {
                    event.add(row.get(0).toString());
                }
                if (row.get(1).toString().matches(timePattern)) {
                    event.add(row.get(1).toString());
                }
                if (row.get(2).toString().matches(subjectPattern)) {
                    event.add(row.get(2).toString());
                }
                if (row.size() > 3 && row.get(3).toString().matches(namePattern)) {
                    event.add(row.get(3).toString());
                }
                result.add(event);
            }
        }
        return result;
    }


}
