package org.gaavr.service;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import lombok.RequiredArgsConstructor;
import org.gaavr.config.Constants;
import org.gaavr.config.GoogleConfig;
import org.gaavr.model.EventDTO;
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
    private final Constants constants;

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

    public List<EventDTO> getListOfEvents() throws GeneralSecurityException, IOException, InterruptedException {
        String spreadsheetId = googleConfig.getSpreadsheetId();
        String range = googleConfig.getMainList() + "!" + constants.getWholeRange();

        Sheets service = googleAuthorizeUtil.getSheetsService();
        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();

        List<List<Object>> values = response.getValues();
        List<EventDTO> result = new ArrayList<>();

        String datePattern = "[0-9]{2}.[0-9]{2}.[0-9]{4}(г\\.)?( \\([а-яА-Я]+\\))?";
        String timePattern = "[0-9]{2}.[0-9]{2}-[0-9]{2}.[0-9]{2}";

        String dateTime = "";
        String subject = "";

        if (values == null || values.isEmpty()) {
            System.out.println("No data found.");
        } else {
            for (List<Object> row : values) {
                if (row.size() < 2) {
                    continue;
                }

                if (row.get(0).toString().matches(datePattern)) {
                    dateTime = row.get(0).toString().replaceFirst("(г\\.)?( \\([а-яА-Я]+\\))?", "") + " " + row.get(1).toString(); // очистим дату от лишних символов
                    if (row.size() > 2) {
                        subject = row.get(2).toString();
                    }

                    if(!dateTime.isEmpty() && !subject.isEmpty()) {
                        result.add(new EventDTO(dateTime, subject, "Преподаватель"));
                        // Reset variables for the next iteration
                        dateTime = "";
                        subject = "";
                    }
                } else {
//                System.out.println("Invalid date row: " + (values.indexOf(row) + 1)); // Printing the row number of the invalid date
                }
            }
        }

        // Вывод результатов
        for (EventDTO dto : result) {
            System.out.println("---------------");
            System.out.println(dto.getDate());
            System.out.println(dto.getSubject());
            System.out.println(dto.getTeacher());
        }

        return result;
    }


}
