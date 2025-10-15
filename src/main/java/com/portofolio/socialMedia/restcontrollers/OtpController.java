package com.portofolio.socialMedia.restControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.portofolio.socialMedia.services.OtpService;
import com.portofolio.socialMedia.utils.ApiResponseWrapper;

@RestController
@RequestMapping("/api/otp")
public class OtpController {

    @Autowired
    private OtpService otpService;
    
    @PostMapping("/send")
    public ResponseEntity<ApiResponseWrapper<String>> getOtp(
        @RequestParam String email
        ) {
    
        otpService.sendOtp(email);

        return ResponseEntity.ok(
            new ApiResponseWrapper<>(
                "Success send otp to " + email, 
                null));   
    }
}
