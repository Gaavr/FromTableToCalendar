package org.gaavr.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("telegram")
@Getter
@Setter
public class TelegramConfig {

    @Value("${name}")
    private String name;

    @Value("${token}")
    private String token;
}
