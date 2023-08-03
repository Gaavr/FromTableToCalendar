package org.gaavr.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class TelegramConfig {

    @Value("${telegram.name}")
    private String name;

    @Value("${telegram.token}")
    private String token;
}