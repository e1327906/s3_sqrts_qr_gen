package com.qre.cmel.ecommsvcs.sdk.service.impl;

import com.qre.cmel.ecommsvcs.sdk.common.ECommSvc;
import com.qre.cmel.ecommsvcs.sdk.config.ECommSvcSdkProperties;
import com.qre.cmel.ecommsvcs.sdk.dto.MessageDto;
import com.qre.cmel.ecommsvcs.sdk.service.ECommSvcService;
import com.qre.cmel.ecommsvcs.sdk.service.EmailService;
import com.qre.cmel.ecommsvcs.sdk.service.SmsService;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;

@Service
public class ECommSvcServiceImpl implements ECommSvcService {

    /**
     * emailService
     */
    private final EmailService emailService;

    /**
     * smsService
     */
    private final SmsService smsService;

    /**
     * eCommSvcSdkProperties
     */
    private final ECommSvcSdkProperties eCommSvcSdkProperties;

    /**
     * @param emailService
     * @param smsService
     * @param eCommSvcSdkProperties
     */
    public ECommSvcServiceImpl(EmailService emailService,
                               SmsService smsService,
                               ECommSvcSdkProperties eCommSvcSdkProperties) {
        this.emailService = emailService;
        this.smsService = smsService;
        this.eCommSvcSdkProperties = eCommSvcSdkProperties;
    }

    /**
     * @param messageDto
     */
    @Override
    public void send(MessageDto messageDto) throws MessagingException, IOException {

        ECommSvc eCommSvc = eCommSvcSdkProperties.getService();

        switch (eCommSvc) {
            case email:
                emailService.send(messageDto);
                break;
            case sms:
                smsService.send(messageDto);
                break;
            default:
                // Handle unknown message broker
                throw new UnsupportedOperationException("Unknown Electronic Communication Services : " + eCommSvc);
        }

    }
}
