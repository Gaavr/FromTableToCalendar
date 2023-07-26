package org.gaavr.controller;

import org.gaavr.service.GoogleSheetsReaderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class CommonController {

    private final GoogleSheetsReaderService googleSheetsReaderService;

    public CommonController(GoogleSheetsReaderService googleSheetsReaderService) {
        this.googleSheetsReaderService = googleSheetsReaderService;
    }

    @GetMapping("/read-google-sheets")
    public List<List<Object>> readGoogleSheets(@RequestParam String range) throws IOException {
        return googleSheetsReaderService.readDataFromGoogleSheets(range);
    }
}
