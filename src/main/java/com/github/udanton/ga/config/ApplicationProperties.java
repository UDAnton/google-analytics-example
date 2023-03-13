package com.github.udanton.ga.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "google.analytics")
public class ApplicationProperties {
    private String url;
    private String apiSecret;
    private String measurementId;
}
