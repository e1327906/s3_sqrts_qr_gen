package com.qre.tg.query.api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
@Data
public class ApplicationProperties {

    private String emailSubject;

    private String emailMessage;

    private String tgUrl;

    private String privateKeyPath;

    private String publicKeyPath;
}
