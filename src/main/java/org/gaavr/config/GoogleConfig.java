package org.gaavr.config;

import lombok.Getter;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@ConfigurationProperties(prefix = "google")
public class GoogleConfig {

    private String spreadsheetId;
    private String credentialsPath;
    private String applicationName;

}
