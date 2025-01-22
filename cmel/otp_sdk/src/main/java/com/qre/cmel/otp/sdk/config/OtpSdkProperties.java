
package com.qre.cmel.otp.sdk.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:ecommsvcs_sdk.properties")
@ConfigurationProperties(prefix = "app.ecommsvcs.sdk")
@Data
public class OtpSdkProperties {
    private int expireMins = 1;
}
