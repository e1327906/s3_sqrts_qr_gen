package com.qre.tg.query.api.controller.impl;

import com.qre.cmel.ecommsvcs.sdk.dto.MessageDto;
import com.qre.cmel.ecommsvcs.sdk.service.EmailService;
import com.qre.cmel.ecommsvcs.sdk.service.SmsService;
import com.qre.cmel.otp.sdk.service.OtpService;
import com.qre.tg.dto.base.APIResponse;
import com.qre.tg.dto.user.OtpRequest;
import com.qre.tg.query.api.config.ApplicationProperties;
import com.qre.tg.query.api.controller.OtpController;
import com.qre.tg.query.api.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.IOException;

import static com.qre.tg.query.api.common.Constants.BASE_URL;

@RestController
@RequestMapping("/api/v1/otp")
public class OtpControllerImpl implements OtpController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final OtpService otpService;

    private final EmailService emailService;

    private final SmsService smsService;

    private final ApplicationProperties applicationProperties;

    private final UserService userService;

    public OtpControllerImpl(OtpService otpService,
                             EmailService emailService,
                             SmsService smsService,
                             UserService userService,
                             ApplicationProperties applicationProperties) {
        this.otpService = otpService;
        this.emailService = emailService;
        this.smsService = smsService;
        this.userService = userService;
        this.applicationProperties = applicationProperties;
    }

    @PostMapping("/SendOtp")
    @Override
    public ResponseEntity<APIResponse> sendOtp(OtpRequest otpRequest) throws MessagingException, IOException {

        logger.info("Method:{}", "sendOtp");

        int otp = otpService.generateOTP(otpRequest.getUserName());
        String message = applicationProperties.getEmailMessage().replace("$", String.valueOf(otp));

        MessageDto messageDto = MessageDto.builder()
                .message(message)
                .subject(applicationProperties.getEmailSubject())
                .to(otpRequest.getUserName())
                .build();
        if (otpRequest.isEmailOrPhone()) {
            emailService.send(messageDto);
        } else {
            smsService.send(messageDto);
        }

        APIResponse apiResponse = new APIResponse(String.valueOf(HttpStatus.OK.value()),
                HttpStatus.OK.getReasonPhrase(), null);

        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/ValidateOtp")
    @Override
    public ResponseEntity<APIResponse> validateOtp(OtpRequest otpRequest) {

        logger.info("Method:{}", "validateOtp");
        boolean isValid = otpService.validateOTP(otpRequest.getUserName(), otpRequest.getOtpNum());
        APIResponse apiResponse;
        if (isValid) {
            logger.info("validated otp");
            //userService.verify(otpDto.getUserName(), otpDto.isEmailOrPhone());
            apiResponse = APIResponse.builder()
                    .responseCode(String.valueOf(HttpStatus.OK.value()))
                    .responseMsg(HttpStatus.OK.getReasonPhrase())
                    .responseData(null)
                    .build();

        } else {
            apiResponse = APIResponse.builder()
                    .responseCode(String.valueOf(HttpStatus.NOT_FOUND.value()))
                    .responseMsg(HttpStatus.NOT_FOUND.getReasonPhrase())
                    .responseData(null)
                    .build();
        }

        return ResponseEntity.ok(apiResponse);
    }
}
