package org.gaavr.google;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import lombok.RequiredArgsConstructor;
import org.gaavr.config.GoogleConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.gaavr.google.GoogleSheetsServiceUtil;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GoogleSheetsReader {

    private final Sheets sheetsService;
    private final GoogleConfig googleConfig;
    private final GoogleSheetsServiceUtil googleSheetsServiceUtil = null;

    @Autowired
    public GoogleSheetsReader(GoogleConfig googleConfig, org.gaavr.google.GoogleSheetsServiceUtil googleSheetsServiceUtil) throws IOException, GeneralSecurityException {
        this.googleConfig = googleConfig;
        this.sheetsService = this.googleSheetsServiceUtil.getSheetsService(googleConfig.getCredentialsPath());
        this.googleSheetsServiceUtil = googleSheetsServiceUtil;
    }

    public List<List<Object>> readData(String range) throws IOException {
        String spreadsheetId = googleConfig.getSpreadsheetId();
        ValueRange response = sheetsService.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        return response.getValues();
    }
}
