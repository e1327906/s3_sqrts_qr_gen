package com.qre.cmel.otp.sdk.service;

import org.springframework.lang.NonNull;

public interface OtpService {
    int generateOTP(@NonNull String username);
    boolean validateOTP(@NonNull String username, int otpNum);
}
