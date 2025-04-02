package com.gathu.gathu.security.service;

import com.gathu.gathu.security.dto.JwtAuthenticationResponse;
import com.gathu.gathu.security.dto.RefreshTokenRequest;
import com.gathu.gathu.security.dto.SigninRequestDTO;
import com.gathu.gathu.security.dto.SignupRequest;
import com.gathu.gathu.security.entity.User;

public interface AuthenticationService {
    public User signup(SignupRequest signupRequest);
    public JwtAuthenticationResponse signin(SigninRequestDTO signinRequestDTO);

    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
    public void logout(String jwt);
    String verifyEmail(String token);
    void resendVerificationEmail(String email);
}
