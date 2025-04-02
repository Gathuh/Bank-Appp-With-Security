package com.gathu.gathu.security.service;


import com.gathu.gathu.security.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface JwtService {

    public String extractUserName(String token);


    public String generateToken(UserDetails userDetails);

    public boolean isTokenValid(String token,UserDetails userDetails);
    public String generateRefreshToken(Map<String ,Object> extractclaims, UserDetails userDetails);
    public String extractTokenType(String token);
}
