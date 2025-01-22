package com.qre.tg.query.api.controller.impl;

import com.qre.cmel.ecommsvcs.sdk.dto.MessageDto;
import com.qre.cmel.ecommsvcs.sdk.service.EmailService;
import com.qre.cmel.ecommsvcs.sdk.service.SmsService;
import com.qre.cmel.otp.sdk.service.OtpService;
import com.qre.tg.dto.auth.AuthenticationRequest;
import com.qre.tg.dto.auth.AuthenticationResponse;
import com.qre.tg.dto.base.APIResponse;
import com.qre.tg.dto.user.OtpRequest;
import com.qre.tg.dto.user.UserRequest;
import com.qre.tg.query.api.config.ApplicationProperties;
import com.qre.tg.query.api.service.impl.AuthenticationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationControllerImpl {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AuthenticationServiceImpl service;

    private final OtpService otpService;

    private final EmailService emailService;

    private final SmsService smsService;

    private final ApplicationProperties applicationProperties;

    @PostMapping("/Register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody UserRequest userRequest) {

        AuthenticationResponse response;
        try {
            response = service.register(userRequest);
        } catch (IllegalArgumentException e) {
            logger.error("Bad Request during user registration: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Unexpected error during user registration", e);
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/Authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        logger.info("AuthenticationControllerImpl.Authenticate {}", request);
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/RefreshToken")
    public ResponseEntity<AuthenticationResponse> refreshToken(
            HttpServletRequest request
    ) {
        return ResponseEntity.ok(service.refreshToken(request));
    }

    @PostMapping("/SendOtp")
    public ResponseEntity<APIResponse> sendOtp(@RequestBody OtpRequest otpRequest) throws MessagingException, IOException {

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
    public ResponseEntity<APIResponse> validateOtp(@RequestBody OtpRequest otpRequest) {

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