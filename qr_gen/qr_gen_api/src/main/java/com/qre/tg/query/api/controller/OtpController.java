package com.qre.tg.query.api.controller;

import com.qre.tg.dto.base.APIResponse;
import com.qre.tg.dto.user.OtpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import javax.mail.MessagingException;
import java.io.IOException;

public interface OtpController {

    ResponseEntity<APIResponse> sendOtp(@RequestBody OtpRequest otpRequest) throws MessagingException, IOException;

    ResponseEntity<APIResponse> validateOtp(@RequestBody OtpRequest otpRequest);
}
