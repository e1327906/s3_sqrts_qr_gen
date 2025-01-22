package com.qre.cmel.otp.sdk.service.impl;

import com.hazelcast.map.IMap;
import com.qre.cmel.cache.service.HazelcastCacheService;
import com.qre.cmel.otp.sdk.config.OtpSdkProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OtpServiceImplTest {

    private OtpServiceImpl otpService;

    @Mock
    private OtpSdkProperties otpSdkProperties;

    @Mock
    private HazelcastCacheService hazelcastCacheService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        otpService = new OtpServiceImpl(otpSdkProperties, hazelcastCacheService);
    }

    @Test
    void generateOTP_shouldReturnValidOTP() {
        String key = "testUser";

        when(hazelcastCacheService.getHazelcastIMap("otpCache")).thenReturn(mock(IMap.class));
        when(hazelcastCacheService.getHazelcastIMap("otpCache").put(eq(key), anyInt(), eq(180L), eq(TimeUnit.SECONDS))).thenReturn(null);

        int generatedOTP = otpService.generateOTP(key);

        assertNotNull(generatedOTP);
    }

    @Test
    void validateOTP_withNegativeOTP_shouldReturnFalse() {
        String key = "testUser";
        int otpNum = -123456;

        boolean isValid = otpService.validateOTP(key, otpNum);

        assertFalse(isValid);
    }

    @Test
    void validateOTP_withZeroOTP_shouldReturnFalse() {
        String key = "testUser";
        int otpNum = 0;

        boolean isValid = otpService.validateOTP(key, otpNum);

        assertFalse(isValid);
    }

    @Test
    void validateOTP_withEmptyKey_shouldReturnFalse() {
        String key = "";
        int otpNum = 123456;

        boolean isValid = otpService.validateOTP(key, otpNum);

        assertFalse(isValid);
    }

    @Test
    void validateOTP_withNullKey_shouldReturnFalse() {
        String key = null;
        int otpNum = 123456;

        boolean isValid = otpService.validateOTP(key, otpNum);

        assertFalse(isValid);
    }

    @Test
    void getOtp_withExistingKey_shouldReturnOtp() {
        String key = "testUser";
        int expectedOtp = 123456;

        when(hazelcastCacheService.getHazelcastIMap("otpCache")).thenReturn(mock(IMap.class));
        when(hazelcastCacheService.getHazelcastIMap("otpCache").get(key)).thenReturn(expectedOtp);

        int actualOtp = otpService.getOtp(key);

        assertEquals(expectedOtp, actualOtp);
    }

    @Test
    void getOtp_withNonExistingKey_shouldReturnZero() {
        String key = "nonExistingUser";

        when(hazelcastCacheService.getHazelcastIMap("otpCache")).thenReturn(mock(IMap.class));
        when(hazelcastCacheService.getHazelcastIMap("otpCache").get(key)).thenReturn(null);

        int actualOtp = otpService.getOtp(key);

        assertEquals(0, actualOtp);
    }
}
