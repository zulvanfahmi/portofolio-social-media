package com.portofolio.socialMedia.services;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.portofolio.socialMedia.configs.RedisService;

@Service
public class OtpService {
    
    @Autowired
    private RedisService redisService;

    @Autowired
    private EmailService emailService;

    private static final SecureRandom random = new SecureRandom();

    private static String generateNumericOtp(int length) {

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            stringBuilder.append(random.nextInt(10));
        }
        return stringBuilder.toString();

    }

    public void sendOtp(String email) {

        String otp = generateNumericOtp(6);

        redisService.saveDataWithExpiration("otp:" + email, otp, 5, TimeUnit.MINUTES);

        emailService.sendEmail(email, "OTP Social Media", otp);
           
    }

    public Boolean verifyOtp(String email, String otp) {

        String getOtpInMemory = redisService.getData("otp:" + email);

        if (getOtpInMemory == null || !getOtpInMemory.equals(otp)) {
            return false;
        }

        redisService.deleteData("otp:" + email);

        return true;

    }

}
