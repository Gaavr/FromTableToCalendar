package org.gaavr.google;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Component
public class GoogleSheetsReader {

    private final GoogleAuthorizeUtil googleAuthorizeUtil;

    public GoogleSheetsReader(GoogleAuthorizeUtil googleAuthorizeUtil) {
        this.googleAuthorizeUtil = googleAuthorizeUtil;
    }

    public List<List<Object>> readData(String spreadsheetId, String range) throws IOException, GeneralSecurityException {
        Sheets sheetsService = googleAuthorizeUtil.getSheetsService();

        ValueRange response = sheetsService.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();

        return response.getValues();
    }
}
