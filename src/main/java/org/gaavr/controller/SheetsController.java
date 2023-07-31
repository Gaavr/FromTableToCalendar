package org.gaavr.controller;

import org.gaavr.config.GoogleConfig;
import org.gaavr.google.GoogleSheetsReader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@RestController
@RequestMapping("/sheets")
public class SheetsController {

    private final GoogleSheetsReader googleSheetsReader;
    private final GoogleConfig googleConfig;

    public SheetsController(GoogleSheetsReader googleSheetsReader, GoogleConfig googleConfig) {
        this.googleSheetsReader = googleSheetsReader;
        this.googleConfig = googleConfig;
    }

    @GetMapping("/data")
    public List<List<Object>> getDataFromSheets() throws IOException, GeneralSecurityException {
        String spreadsheetId = googleConfig.getSpreadsheetId();
        String range = "УБВТ21!A478:B478"; // Замените на нужный диапазон данных
        return googleSheetsReader.readData(spreadsheetId, range);
    }
}
