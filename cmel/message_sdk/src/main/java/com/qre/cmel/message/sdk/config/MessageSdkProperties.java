
package com.qre.cmel.message.sdk.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:message_sdk.properties")
@ConfigurationProperties(prefix = "app.message.sdk")
@Data
public class MessageSdkProperties {
}
