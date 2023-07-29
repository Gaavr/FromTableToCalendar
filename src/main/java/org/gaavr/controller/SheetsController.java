package org.gaavr.controller;

import lombok.RequiredArgsConstructor;
import org.gaavr.config.GoogleConfig;
import org.gaavr.google.GoogleSheetsReader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sheets")
@RequiredArgsConstructor
public class SheetsController {

    private final GoogleSheetsReader googleSheetsReader;
    private final GoogleConfig googleConfig;

    @GetMapping("/read")
    public void readDataFromSheets() {
        String spreadsheetId = googleConfig.getSpreadsheetId();
        String range = "Sheet1!A1:C";
        List<List<Object>> data = googleSheetsReader.readData(spreadsheetId, range);

        // Выводим данные в консоль
        for (List<Object> row : data) {
            for (Object cell : row) {
                System.out.print(cell + "\t");
            }
            System.out.println();
        }
    }
}