package org.gaavr.service;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import lombok.RequiredArgsConstructor;
import org.gaavr.util.GoogleAuthorizeUtil;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GoogleSheetsReaderService {

    private final GoogleAuthorizeUtil googleAuthorizeUtil;

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
}
