package com.portofolio.socialMedia.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portofolio.socialMedia.dto.AuthRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
    @MockBean
    private AuthenticationManager authenticationManager;

    @SuppressWarnings("removal")
    @MockBean
    private UserDetailsService userDetailsService;

    @SuppressWarnings("removal")
    @MockBean
    private JwtService jwtService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testLoginSuccess() throws Exception {
        // given
        AuthRequestDTO request = new AuthRequestDTO("zulvan", "password123");
        UserDetails userDetails = new User("zulvan", "password123", Collections.emptyList());

        when(userDetailsService.loadUserByUsername("zulvan")).thenReturn(userDetails);
        when(jwtService.generateToken(userDetails)).thenReturn("mocked-jwt-token");

        // when & then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Success generate token"))
                .andExpect(jsonPath("$.data").value("mocked-jwt-token"));
    }

    @Test
    void testLoginFail_InvalidUsername() throws Exception {
        // given
        AuthRequestDTO request = new AuthRequestDTO("wrongUser", "password123");

        doThrow(new BadCredentialsException("Bad credentials"))
                .when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

        // when & then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid Username or Password (002)"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    void testLoginFail_InvalidPassword() throws Exception {
        // given
        AuthRequestDTO request = new AuthRequestDTO("zulvan", "wrongPassword");

        doThrow(new BadCredentialsException("Bad credentials"))
                .when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

        // when & then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid Username or Password (002)"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }
}
