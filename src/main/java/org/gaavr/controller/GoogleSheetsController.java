package org.gaavr.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.gaavr.config.GoogleConfig;
import org.gaavr.model.EventDTO;
import org.gaavr.service.GoogleSheetsReaderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@RestController
@RequestMapping("/sheets")
@RequiredArgsConstructor
@Api(value = "Google Sheets Controller", tags = { "Google Sheets" })
public class GoogleSheetsController {

    private final GoogleSheetsReaderService googleSheetsReaderService;
    private final GoogleConfig googleConfig;

    @GetMapping("/data")
    public List<List<Object>> getDataFromSheets() {
        String spreadsheetId = googleConfig.getSpreadsheetId();
        String range = "УБВТ21-2!A213:B213";
        return googleSheetsReaderService.readData(spreadsheetId, range);
    }

    @GetMapping("/events")
    public List<EventDTO> getListOfEvents() throws IOException, GeneralSecurityException, InterruptedException {
        return googleSheetsReaderService.getListOfEvents();
    }
}
