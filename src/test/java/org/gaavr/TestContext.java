package org.gaavr;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationPropertiesScan
@ComponentScan(basePackages = "org.gaavr.config")
@PropertySource("classpath:app-properties.yaml")
public class TestContext {
}
