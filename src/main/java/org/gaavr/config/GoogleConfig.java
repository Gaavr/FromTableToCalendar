package org.gaavr.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class GoogleConfig {

    @Value("${google.spreadsheet.id}")
    private String spreadsheetId;

    @Value("${google.credentials.path}")
    private String credentialsPath;

    @Value("${google.application.name}")
    private String applicationName;
}

