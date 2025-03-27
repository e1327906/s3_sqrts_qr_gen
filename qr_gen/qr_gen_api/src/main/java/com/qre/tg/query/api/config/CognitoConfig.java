package com.qre.tg.query.api.config;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CognitoConfig {

    @Value("${app.accessKey:#{null}}")
    private String accessKey;

    @Value("${app.secretKey:#{null}}")
    private String secretKey;

    @Bean
    public CognitoIdentityProviderClient cognitoClient() {
        // If access key and secret key are provided, use them; otherwise, use default provider (IAM roles or environment variables)
        if (accessKey != null && secretKey != null) {
            return CognitoIdentityProviderClient.builder()
                    .region(Region.AP_SOUTHEAST_1) // Replace with your AWS region
                    .credentialsProvider(StaticCredentialsProvider.create(
                            AwsBasicCredentials.create(accessKey, secretKey)))
                    .build();
        } else {
            return CognitoIdentityProviderClient.builder()
                    .region(Region.AP_SOUTHEAST_1) // Replace with your AWS region
                    .credentialsProvider(DefaultCredentialsProvider.create()) // Uses AWS credentials from environment or IAM role
                    .build();
        }
    }
}