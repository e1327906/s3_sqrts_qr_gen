package com.qre.cmel.otp.sdk.service.impl;

import com.hazelcast.map.IMap;
import com.qre.cmel.cache.service.HazelcastCacheService;
import com.qre.cmel.otp.sdk.config.OtpSdkProperties;
import org.springframework.stereotype.Service;
import com.qre.cmel.otp.sdk.service.OtpService;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class OtpServiceImpl implements OtpService {

    /**
     * otpSdkProperties
     */
    private final OtpSdkProperties otpSdkProperties;

    /**
     * hazelcastCacheService
     */
    private final HazelcastCacheService hazelcastCacheService;

    /**
     *
     * @param otpSdkProperties
     * @param hazelcastCacheService
     */
    public OtpServiceImpl(OtpSdkProperties otpSdkProperties, HazelcastCacheService hazelcastCacheService) {
        super();
        this.otpSdkProperties = otpSdkProperties;
        this.hazelcastCacheService = hazelcastCacheService;
    }

    /**
     * This method is used to push the opt number against Key. Rewrite the OTP if it exists.
     * Using user name  as key
     * @param key
     * @return
     */
    @Override
    public int generateOTP(String key) {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        IMap<String, Integer> otpCache = hazelcastCacheService.getHazelcastIMap("otpCache");
        otpCache.put(key, otp, 180, TimeUnit.SECONDS); //after 3 minutes will expire
        return otp;
    }

    /**
     *
     * @param key
     * @param otpNum
     */
    @Override
    public boolean validateOTP(String key, int otpNum) {
        if (otpNum >= 0) {
            int serverOtp = getOtp(key);
            if (serverOtp > 0) {
                if (otpNum == serverOtp) {
                    clearOTP(key);
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * This method is used to return the OPT number against Key->Key values is username
     * @param key
     * @return
     */
    public int getOtp(String key){
        try{
            IMap<String, Integer> otpCache = hazelcastCacheService.getHazelcastIMap("otpCache");
            return otpCache.get(key);
        }catch (Exception e){
            return 0;
        }
    }

    /**
     * This method is used to clear the OTP catched already
     * @param key
     */
    public void clearOTP(String key){
        IMap<String, Integer> otpCache = hazelcastCacheService.getHazelcastIMap("otpCache");
        otpCache.remove(key);
    }
}
