package com.qre.cmel.ecommsvcs.sdk.config;

import com.qre.cmel.ecommsvcs.sdk.common.ECommSvc;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@Configuration
@PropertySource("classpath:ecommsvcs_sdk.properties")
@ConfigurationProperties(prefix = "app.ecommsvcs.sdk")
@Data
public class ECommSvcSdkProperties {

    /**
     * messageBroker
     */
    private ECommSvc service = ECommSvc.email;

    /**
     * email
     */
    private String fromEmail;

}
