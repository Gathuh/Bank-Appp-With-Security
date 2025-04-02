package com.gathu.gathu.security.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Value("${app.base-url:http://localhost:9000}")
    private String baseUrl;

    @Value("${spring.mail.username:sgathuh@gmail.com}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(String to, String token) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        String verificationLink = baseUrl + "/api/v1/auth/verify-email?token=" + token;

        String plainText = "Welcome to Gathu!\n\n" +
                "Please verify your email by visiting the following link:\n" +
                verificationLink + "\n\n" +
                "If the link doesn't work, copy and paste the URL into your browser.\n" +
                "This link will expire in 24 hours. If you need a new link, you can request one.";

        String htmlContent = String.format("""
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Verify Your Email - Gathu</title>
            <style>
                body {
                    margin: 0;
                    padding: 0;
                    background-color: #f5f5f5;
                    font-family: 'Helvetica Neue', Arial, sans-serif;
                    color: #333333;
                }
                .container {
                    max-width: 600px;
                    margin: 40px auto;
                    background: #ffffff;
                    border: 1px solid #e5e5e5;
                    border-radius: 4px;
                    overflow: hidden;
                }
                .header {
                    text-align: center;
                    padding: 20px 0;
                }
                .header img {
                    max-width: 150px;
                }
                .content {
                    padding: 20px 40px;
                    text-align: left;
                    font-size: 16px;
                    line-height: 1.6;
                }
                .greeting {
                    font-size: 24px;
                    font-weight: bold;
                    color: #333333;
                    margin-bottom: 10px;
                }
                .channel {
                    color: #2ab27b;
                    font-weight: bold;
                    font-size: 18px;
                    margin: 20px 0 10px;
                }
                .message {
                    background: #f9f9f9;
                    border-left: 4px solid #2ab27b;
                    padding: 15px;
                    margin: 20px 0;
                    font-size: 16px;
                    line-height: 1.5;
                }
                .cta-button {
                    display: block;
                    width: 80%%;
                    max-width: 200px;
                    margin: 20px auto;
                    padding: 12px;
                    background-color: #2ab27b;
                    color: white;
                    font-size: 16px;
                    font-weight: bold;
                    text-align: center;
                    border-radius: 4px;
                    text-decoration: none;
                    transition: background-color 0.3s ease;
                }
                .cta-button:hover {
                    background-color: #218c66;
                }
                .link {
                    word-break: break-all;
                    color: #1264a3;
                    text-decoration: none;
                }
                .link:hover {
                    text-decoration: underline;
                }
                .footer {
                    padding: 20px;
                    text-align: center;
                    font-size: 12px;
                    color: #666666;
                    border-top: 1px solid #e5e5e5;
                }
                .footer a {
                    color: #1264a3;
                    text-decoration: none;
                }
                @media only screen and (max-width: 600px) {
                    .container {
                        width: 100%%;
                        margin: 20px auto;
                    }
                    .content {
                        padding: 15px;
                    }
                    .greeting {
                        font-size: 20px;
                    }
                    .channel {
                        font-size: 16px;
                    }
                    .cta-button {
                        padding: 10px;
                        font-size: 14px;
                    }
                }
            </style>
        </head>
        <body>
            <div class="container">
                <div class="header">
                    <img src="https://gathu.com/logo.png" alt="Gathu Logo">
                </div>
                <div class="content">
                    <div class="greeting">Hi there,</div>
                    <p>You have a new message from the Gathu team (gathu@gathu.com).</p>
                    <div class="channel">#email-verification</div>
                    <div class="message">
                        <strong>Welcome to Gathu!</strong><br>
                        Please verify your email address by clicking the button below.<br>
                        This link expires in 24 hours. Need a new one? Request it anytime.
                    </div>
                    <a href="%s" class="cta-button">Verify Email</a>
                    <p>Or use this link:<br><a href="%s" class="link">%s</a></p>
                </div>
                <div class="footer">
                    © 2024 Gathu. All rights reserved.<br>
                    <a href="https://gathu.com/privacy-policy">Privacy Policy</a>
                </div>
            </div>
        </body>
        </html>
        """, verificationLink, verificationLink, verificationLink);

        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject("Verify Your Email Address - Gathu");
        helper.setText(plainText, htmlContent);

        try {
            mailSender.send(message);
            logger.info("Verification email sent successfully to: {}", to);
        } catch (Exception e) {
            logger.error("Failed to send verification email to {}: {}", to, e.getMessage(), e);
            throw e;
        }
    }

    public void sendVerificationSuccessEmail(String to) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        String loginLink = baseUrl + "/login";

        String plainText = "Great news!\n\n" +
                "Your email has been successfully verified. You can now log in to Gathu:\n" +
                loginLink + "\n\n" +
                "If the link doesn't work, copy and paste the URL into your browser.\n" +
                "We’re excited to have you on board!";

        String htmlContent = String.format("""
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Email Verified - Gathu</title>
            <style>
                body {
                    margin: 0;
                    padding: 0;
                    background-color: #f5f5f5;
                    font-family: 'Helvetica Neue', Arial, sans-serif;
                    color: #333333;
                }
                .container {
                    max-width: 600px;
                    margin: 40px auto;
                    background: #ffffff;
                    border: 1px solid #e5e5e5;
                    border-radius: 4px;
                    overflow: hidden;
                }
                .header {
                    text-align: center;
                    padding: 20px 0;
                }
                .header img {
                    max-width: 150px;
                }
                .content {
                    padding: 20px 40px;
                    text-align: left;
                    font-size: 16px;
                    line-height: 1.6;
                }
                .greeting {
                    font-size: 24px;
                    font-weight: bold;
                    color: #333333;
                    margin-bottom: 10px;
                }
                .channel {
                    color: #2ab27b;
                    font-weight: bold;
                    font-size: 18px;
                    margin: 20px 0 10px;
                }
                .message {
                    background: #f9f9f9;
                    border-left: 4px solid #2ab27b;
                    padding: 15px;
                    margin: 20px 0;
                    font-size: 16px;
                    line-height: 1.5;
                }
                .cta-button {
                    display: block;
                    width: 80%%;
                    max-width: 200px;
                    margin: 20px auto;
                    padding: 12px;
                    background-color: #2ab27b;
                    color: white;
                    font-size: 16px;
                    font-weight: bold;
                    text-align: center;
                    border-radius: 4px;
                    text-decoration: none;
                    transition: background-color 0.3s ease;
                }
                .cta-button:hover {
                    background-color: #218c66;
                }
                .link {
                    word-break: break-all;
                    color: #1264a3;
                    text-decoration: none;
                }
                .link:hover {
                    text-decoration: underline;
                }
                .footer {
                    padding: 20px;
                    text-align: center;
                    font-size: 12px;
                    color: #666666;
                    border-top: 1px solid #e5e5e5;
                }
                .footer a {
                    color: #1264a3;
                    text-decoration: none;
                }
                @media only screen and (max-width: 600px) {
                    .container {
                        width: 100%%;
                        margin: 20px auto;
                    }
                    .content {
                        padding: 15px;
                    }
                    .greeting {
                        font-size: 20px;
                    }
                    .channel {
                        font-size: 16px;
                    }
                    .cta-button {
                        padding: 10px;
                        font-size: 14px;
                    }
                }
            </style>
        </head>
        <body>
            <div class="container">
                <div class="header">
                    <img src="https://media.licdn.com/dms/image/v2/D4D03AQH52x9bAop4zw/profile-displayphoto-shrink_200_200/B4DZWFX_GGG8AY-/0/1741699413799?e=1748476800&v=beta&t=utrup5gUYlOSlM4Zsw_pxwWTJZIiGbEvLA-Wo-VwvAY" alt="Gathu Logo">
                </div>
                <div class="content">
                    <div class="greeting">Hi there,</div>
                    <p>You have a new message from the Gathu team (gathu@gathu.com).</p>
                    <div class="channel">#welcome</div>
                    <div class="message">
                        <strong>Email verified successfully!</strong><br>
                        You're all set to start using Gathu. Click the button below to log in and get started.<br>
                        We’re excited to have you on board!
                    </div>
                    <a href="%s" class="cta-button">Log In Now</a>
                    <p>Or use this link:<br><a href="%s" class="link">%s</a></p>
                </div>
                <div class="footer">
                    © 2024 Gathu. All rights reserved.<br>
                    <a href="https://gathu.com/privacy-policy">Privacy Policy</a>
                </div>
            </div>
        </body>
        </html>
        """, loginLink, loginLink, loginLink);

        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject("Email Verified - Welcome to Gathu!");
        helper.setText(plainText, htmlContent);

        try {
            mailSender.send(message);
            logger.info("Verification success email sent successfully to: {}", to);
        } catch (Exception e) {
            logger.error("Failed to send verification success email to {}: {}", to, e.getMessage(), e);
            throw e;
        }
    }
}