package com.gathu.gathu.security.controller;

import com.gathu.gathu.security.dto.JwtAuthenticationResponse;
import com.gathu.gathu.security.dto.RefreshTokenRequest;
import com.gathu.gathu.security.dto.SigninRequestDTO;
import com.gathu.gathu.security.dto.SignupRequest;
import com.gathu.gathu.security.email.EmailRequest;
import com.gathu.gathu.security.entity.User;
import com.gathu.gathu.security.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
//@NoArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AuthenticationController {
private final AuthenticationService authenticationService;




    @PostMapping("/register")
    public ResponseEntity<String> addUser(@RequestBody SignupRequest signupRequest) {
        authenticationService.signup(signupRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("User registered successfully. Please check your email to verify your account.");
    }

        @PostMapping("/login")
        public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody SigninRequestDTO signinRequestDTO){
        return ResponseEntity.status(HttpStatus.OK).body(authenticationService.signin(signinRequestDTO));


}

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.status(HttpStatus.OK).body("Great Logged in");
    }
    @PostMapping("/refresh-token")
    public ResponseEntity<JwtAuthenticationResponse> refreshtoken(@RequestBody RefreshTokenRequest refreshTokenRequest){
        return ResponseEntity.status(HttpStatus.OK).body(authenticationService.refreshToken(refreshTokenRequest));


    }



    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        return ResponseEntity.status(HttpStatus.OK).body(authenticationService.verifyEmail(token));
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<String> resendVerificationEmail(@RequestBody EmailRequest emailRequest) {
        authenticationService.resendVerificationEmail(emailRequest.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body("Verification email resent successfully.");
    }



//     NO NEED FOR THIS FRONTEND WILL HANDLE THIS

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, "Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing or invalid Authorization header");
        }

        final String jwt = authHeader.substring(7);
        authenticationService.logout(jwt);
        return ResponseEntity.status(HttpStatus.OK).body("Logged out successfully");
    }
}







