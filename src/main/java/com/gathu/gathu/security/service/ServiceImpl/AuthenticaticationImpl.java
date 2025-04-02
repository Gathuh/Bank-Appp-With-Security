package com.gathu.gathu.security.service.ServiceImpl;

import com.gathu.gathu.security.dto.JwtAuthenticationResponse;
import com.gathu.gathu.security.dto.RefreshTokenRequest;
import com.gathu.gathu.security.dto.SigninRequestDTO;
import com.gathu.gathu.security.dto.SignupRequest;
import com.gathu.gathu.security.email.EmailService;
import com.gathu.gathu.security.entity.Customer;
import com.gathu.gathu.security.entity.Role;
import com.gathu.gathu.security.entity.User;
import com.gathu.gathu.security.exceptions.EmailSendingException;
import com.gathu.gathu.security.repository.CustomerRepository;
import com.gathu.gathu.security.repository.UserRepository;
import com.gathu.gathu.security.service.AuthenticationService;
import com.gathu.gathu.security.service.JwtService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticaticationImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository; // Added
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final EmailService emailService;

    public User signup(SignupRequest signupRequest) {
        if (userRepository.findByEmail(signupRequest.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already in use: " + signupRequest.getEmail());
        }

        // Create the User
        User user = new User();
        user.setEmail(signupRequest.getEmail());
        user.setFirstName(signupRequest.getFirstname());
        user.setLastName(signupRequest.getLastname());
        user.setUsername(signupRequest.getUsername());
        user.setRole(Role.USER);
        user.setEmailVerified(false);
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));

        String verificationToken = UUID.randomUUID().toString();
        user.setVerificationToken(verificationToken);
        user.setVerificationTokenExpiry(LocalDateTime.now().plusHours(2));
        userRepository.save(user);

        // Create a corresponding Customer
        Customer customer = new Customer();
        customer.setName(signupRequest.getFirstname() + " " + signupRequest.getLastname());
        customer.setEmail(signupRequest.getEmail());
        customer.setUser(user);
        customerRepository.save(customer);

        // Send verification email
        try {
            emailService.sendVerificationEmail(user.getEmail(), verificationToken);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send verification email", e);
        }

        return user;
    }

    public JwtAuthenticationResponse signin(SigninRequestDTO signinRequestDTO) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinRequestDTO.getEmail(), signinRequestDTO.getPassword()));

        var user = userRepository.findByEmail(signinRequestDTO.getEmail()).orElseThrow(() -> new IllegalArgumentException("Invalid User, Not Found"));
        var token = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);
        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();

        // Check if email is verified
        if (!user.isEmailVerified()) {
            throw new IllegalStateException("Email not verified. Please check your inbox to verify your email.");
        }

        // Reset tokenRevoked on successful login (we'll change this later for logout)
        user.setUserLogedout(false);
        userRepository.save(user);
        jwtAuthenticationResponse.setRefreshToken(refreshToken);
        jwtAuthenticationResponse.setToken(token);

        return jwtAuthenticationResponse;
    }

    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String email = jwtService.extractUserName(refreshTokenRequest.getToken());

        User user = userRepository.findByEmail(email).orElseThrow();
        if (jwtService.isTokenValid(refreshTokenRequest.getToken(), user)) {
            var token = jwtService.generateToken(user);
            JwtAuthenticationResponse authenticationResponse = new JwtAuthenticationResponse();

            authenticationResponse.setToken(token);
            authenticationResponse.setRefreshToken(refreshTokenRequest.getToken());
            return authenticationResponse;
        }
        return null;
    }

    private void revokeToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found"));
        user.setTokenRevoked(true);
        userRepository.save(user);
    }

    @Override
    public void logout(String jwt) {
        String userEmail = jwtService.extractUserName(jwt);
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        user.setUserLogedout(true);
        userRepository.save(user);

        SecurityContextHolder.clearContext();
    }

    @Override
    public String verifyEmail(String randomstring) {
        User user = userRepository.findByVerificationToken(randomstring)
                .orElseThrow(() -> new IllegalArgumentException("Invalid verification token"));

        // Check if token has expired
        if (user.getVerificationTokenExpiry() == null || user.getVerificationTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Verification token has expired. Please request a new verification email.");
        }

        // Verify the email
        user.setEmailVerified(true);
        user.setVerificationToken(null);
        user.setVerificationTokenExpiry(null);
        userRepository.save(user);

        return "Email verified successfully! You can now log in.";
    }

    @Override
    public void resendVerificationEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found"));

        // Check if already verified
        if (user.isEmailVerified()) {
            throw new IllegalStateException("Email is already verified.");
        }

        // Generate a new token and update expiry
        String newToken = UUID.randomUUID().toString();
        user.setVerificationToken(newToken);
        user.setVerificationTokenExpiry(LocalDateTime.now().plusHours(24));
        userRepository.save(user);

        // Send new verification email
        try {
            emailService.sendVerificationEmail(user.getEmail(), newToken);
        } catch (MessagingException e) {
            throw new EmailSendingException("Failed to send verification email", e);
        }
    }
}