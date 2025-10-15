package com.portofolio.socialMedia.restControllers;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.portofolio.socialMedia.security.JwtService;
import com.portofolio.socialMedia.services.OtpService;

@WebMvcTest(OtpController.class)
@AutoConfigureMockMvc(addFilters = false)
public class OtpRestControllerTest {

    @SuppressWarnings("removal")
    @MockBean
    private OtpService otpService;
    
    @Autowired
    private MockMvc mockMvc;
    
    @SuppressWarnings("removal")
    @MockBean
    private JwtService jwtService;

    @Test
    void testGetOtp_Success() throws Exception {

        // given
        String email = "test@example.com";
        doNothing().when(otpService).sendOtp(email);

        // when & then
        mockMvc.perform(post("/api/otp/send")
                        .param("email", email))
                .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Success send otp to " + email))
            .andExpect(jsonPath("$.data").doesNotExist());

        // verify kalau otpService dipanggil dengan email yg benar
        verify(otpService).sendOtp(email);

    }

}
