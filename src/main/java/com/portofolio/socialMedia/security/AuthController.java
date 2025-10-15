package com.portofolio.socialMedia.security;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portofolio.socialMedia.dto.AuthRequestDTO;
import com.portofolio.socialMedia.utils.ApiResponseWrapper;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    public AuthController(
        AuthenticationManager authenticationManager, 
        UserDetailsService userDetailsService, 
        JwtService jwtService) {

        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;

    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseWrapper<String>> authenticate(
        @RequestBody AuthRequestDTO request
        ) {

        try {
            authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

            return ResponseEntity
            .ok(new ApiResponseWrapper<>("Success generate token", jwtService.generateToken(userDetails)));

        } catch (BadCredentialsException e) {

            return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(new ApiResponseWrapper<>("Invalid Username or Password (002)", null));
            
        }
    }
}