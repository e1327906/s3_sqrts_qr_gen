package com.qre.tg.query.api.controller.impl;

import com.qre.cmel.ecommsvcs.sdk.dto.MessageDto;
import com.qre.cmel.ecommsvcs.sdk.service.EmailService;
import com.qre.cmel.ecommsvcs.sdk.service.SmsService;
import com.qre.cmel.otp.sdk.service.OtpService;
import com.qre.tg.dto.base.APIResponse;
import com.qre.tg.dto.user.OtpRequest;
import com.qre.tg.query.api.config.ApplicationProperties;
import com.qre.tg.query.api.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.mail.MessagingException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OtpControllerImplTest {

    @Mock
    private OtpService otpService;

    @Mock
    private EmailService emailService;

    @Mock
    private SmsService smsService;

    @Mock
    private ApplicationProperties applicationProperties;

    @InjectMocks
    private OtpControllerImpl otpController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendOtpWithEmail() throws MessagingException, IOException {
        OtpRequest otpRequest = OtpRequest.builder()
                .emailOrPhone(true)
                .userName("user@example.com")
                .build();
        when(otpService.generateOTP(otpRequest.getUserName())).thenReturn(123);
        when(applicationProperties.getEmailMessage()).thenReturn("Your OTP is $");
        when(applicationProperties.getEmailSubject()).thenReturn("OTP Subject");

        ResponseEntity<APIResponse> responseEntity = otpController.sendOtp(otpRequest);

        assertEquals(String.valueOf(HttpStatus.OK.value()), responseEntity.getBody().getResponseCode());
        verify(emailService, times(1)).send(any(MessageDto.class));
    }

    @Test
    void testSendOtpWithSms() throws MessagingException, IOException {
        OtpRequest otpRequest =  OtpRequest.builder()
                .emailOrPhone(false)
                .userName("1234567890")
                .build();
        when(otpService.generateOTP(otpRequest.getUserName())).thenReturn(456);
        when(applicationProperties.getEmailMessage()).thenReturn("Your OTP is $");
        when(applicationProperties.getEmailSubject()).thenReturn("OTP Subject");

        ResponseEntity<APIResponse> responseEntity = otpController.sendOtp(otpRequest);

        assertEquals(String.valueOf(HttpStatus.OK.value()), responseEntity.getBody().getResponseCode());
        verify(smsService, times(1)).send(any(MessageDto.class));
    }


    @Test
    void testValidateOtpValid() {
        OtpRequest otpRequest = OtpRequest.builder()
                .emailOrPhone(true)
                .userName("user@example.com")
                .otpNum(123)
                .build();
        when(otpService.validateOTP(otpRequest.getUserName(), otpRequest.getOtpNum())).thenReturn(true);

        ResponseEntity<APIResponse> responseEntity = otpController.validateOtp(otpRequest);

        assertEquals(String.valueOf(HttpStatus.OK.value()), responseEntity.getBody().getResponseCode());
    }

    @Test
    void testValidateOtpInvalid() {
        OtpRequest otpRequest = OtpRequest.builder()
                .emailOrPhone(true)
                .userName("user@example.com")
                .otpNum(123)
                .build();

        when(otpService.validateOTP(otpRequest.getUserName(), otpRequest.getOtpNum())).thenReturn(false);

        ResponseEntity<APIResponse> responseEntity = otpController.validateOtp(otpRequest);

        assertEquals(String.valueOf(HttpStatus.NOT_FOUND.value()), responseEntity.getBody().getResponseCode());
    }
}
