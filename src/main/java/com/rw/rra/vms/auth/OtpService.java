package com.rw.rra.vms.auth;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@AllArgsConstructor
@Service
@Slf4j
public class OtpService {

    private final Cache<String, String> otpCache;

    public OtpService() {
        this.otpCache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(1000)
                .build();
    }

    String generateOtp(String userEmail, OtpType otpType) {
        var otp = generateOtp();
        String key = generateKey(userEmail, otp, otpType);
        storeOtp(key, otp);
        return otp;
    }

    boolean verifyOtp(String userEmail, String otp, OtpType otpType) {
        String key = generateKey(userEmail, otp, otpType);
        String storedOtp = getOtp(key);
        if (storedOtp != null && storedOtp.equals(otp)) {
            deleteOtp(key);
            return true;
        }
        return false;
    }

    private String getOtp(String key) {
        return otpCache.getIfPresent(key);
    }

    private void deleteOtp(String key) {
        otpCache.invalidate(key);
    }

    private String generateKey(String userEmail, String otp, OtpType otpType) {
        return String.format("%s:%s:%s", otpType.toString(), userEmail, otp);
    }

    private void storeOtp(String key, String otp) {
        otpCache.put(key, otp);
        log.info("Storing OTP successfully");
    }

    private String generateOtp() {
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int digit = (int) (Math.random() * 10);
            otp.append(digit);
        }
        return otp.toString();
    }
}