package org.gaavr.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("google")
@Getter
@Setter
public class GoogleConfig {

    @Value("${google.spreadsheet.id}")
    private String spreadsheetId;

    @Value("${google.spreadsheet.mainList}")
    private String mainList;

    @Value("${google.credentials.path}")
    private String credentialsPath;

    @Value("${google.application.name}")
    private String applicationName;

    @Value("${google.calendar.id}")
    private String calendarId;
}