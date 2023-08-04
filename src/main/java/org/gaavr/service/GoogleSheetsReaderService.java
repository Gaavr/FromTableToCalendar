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

        String timePattern = "[0-9]{2}.[0-9]{2}-[0-9]{2}.[0-9]{2}";
        String datePattern = "[0-9]{2}.[0-9]{2}.[0-9]{4}";
        String subjectPattern = "^[А-Яа-я].*";
        String namePattern = "[А-Я][а-я]*\\s[А-Я].\\s?[А-Я].?";

        String date = "";
        String time = "";
        String subject = "";
        String teacher = "";

        if (values == null || values.isEmpty()) {
            System.out.println("No data found.");
        } else {
            for (List<Object> row : values) {
                if (row.size() < 3) continue; // Если в строке меньше 3 элементов, пропускаем ее

                // RegEx validation
                if (row.size() > 3 && row.get(3).toString().matches(namePattern)) {
                    teacher = row.get(3).toString();
                    EventDTO event = new EventDTO(date + " " + time, subject, teacher);
                    // Only add the event if it does not exist in the result list
                    if (!result.contains(event)) {
                        result.add(event);
                    }
                    // Reset date, time and subject for next round
                    date = "";
                    time = "";
                    subject = "";
                } else {
                    if (row.get(0).toString().matches(datePattern)) {
                        date = row.get(0).toString();
                    }
                    if (row.get(1).toString().matches(timePattern)) {
                        time = row.get(1).toString();
                    }
                    if (row.get(2).toString().matches(subjectPattern)) {
                        subject = row.get(2).toString();
                    }
                }
            }
        }
        return result;
    }
}
