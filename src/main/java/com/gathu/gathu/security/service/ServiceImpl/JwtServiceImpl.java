package com.gathu.gathu.security.service.ServiceImpl;

import com.gathu.gathu.security.entity.User;
import com.gathu.gathu.security.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Service
public class JwtServiceImpl implements JwtService {


    @Value("${jwt.secret}")  // Inject secret key from application.yaml or environment
    private String SECRET_KEY;

    @Override
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "access");
        return Jwts.builder()
                .setClaims(claims) // Include the claims with "type": "access"
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + (1000*60*10*24))) // 10 minutes
                .signWith(getSiginKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>(extraClaims); // Merge extraClaims
        claims.put("type", "refresh"); // Ensure "type": "refresh" is set
        return Jwts.builder()
                .setClaims(claims) // Use the merged claims
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + (1000L * 60 * 60*24*10))) // 10 days
                .signWith(getSiginKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private <T> T extractClaims(String token, Function<Claims,T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Key getSiginKey(){
        byte[] key= Decoders.BASE64URL.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(key);


    }

    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .setSigningKey(getSiginKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUserName(String token){

        return extractClaims(token, Claims::getSubject);
    }


    public boolean isTokenValid(String token,UserDetails userDetails){
        final String username=extractUserName(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);

    }

    private boolean isTokenExpired(String token){
        return extractClaims(token,Claims::getExpiration).before(new Date());
    }

    public String extractTokenType(String token) {
        return extractClaims(token, claims -> claims.get("type", String.class));
    }



}



