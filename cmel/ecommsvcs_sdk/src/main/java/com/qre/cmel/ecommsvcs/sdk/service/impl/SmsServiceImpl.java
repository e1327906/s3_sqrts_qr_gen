
package com.qre.cmel.ecommsvcs.sdk.service.impl;

import com.qre.cmel.ecommsvcs.sdk.dto.MessageDto;
import com.qre.cmel.ecommsvcs.sdk.service.SmsService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsServiceImpl implements SmsService {

    /**
     * ACCOUNT_SID
     */
    @Value("${twilio.account_sid}")
    private String ACCOUNT_SID;

    /**
     * AUTH_TOKEN
     */
    @Value("${twilio.auth_token}")
    private String AUTH_TOKEN;

    /**
     * TWILIO_NUMBER
     */
    @Value("${twilio.twilio_number}")
    private String TWILIO_NUMBER;

    /**
     *
     * @param messageDto
     */
    @Override
    public void send(MessageDto messageDto) {

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message.creator(
                new PhoneNumber(messageDto.getTo()),
                new PhoneNumber(TWILIO_NUMBER),
                messageDto.getMessage())
                .create();

    }
}
