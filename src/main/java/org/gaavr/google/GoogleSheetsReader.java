package org.gaavr.google;

import com.google.api.services.sheets.v4.model.ValueRange;
import lombok.AllArgsConstructor;
import org.gaavr.config.GoogleConfig;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.api.services.sheets.v4.Sheets;


@Component
@AllArgsConstructor
public class GoogleSheetsReader {

    private final GoogleConfig googleConfig;
    private final GoogleAuthorizeUtil googleAuthorizeUtil;

    public List<List<String>> readData(String spreadsheetId, String range) {
        try {
            Sheets sheetsService = googleAuthorizeUtil.getSheetsService();

            ValueRange response = sheetsService.spreadsheets().values()
                    .get(spreadsheetId, range)
                    .execute();

            List<List<Object>> values = response.getValues();
            List<List<String>> result = new ArrayList<>();

            if (values != null) {
                for (List<Object> row : values) {
                    List<String> stringRow = new ArrayList<>();
                    for (Object cell : row) {
                        stringRow.add(String.valueOf(cell));
                    }
                    result.add(stringRow);
                }
            }

            return result;
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
            System.out.println("readData упал");
            return null;
        }
    }


}
