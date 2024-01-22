package org.example.librarymanagement.config.security.jwtConfig;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.librarymanagement.entity.Session;
import org.example.librarymanagement.exception.exception.BadRequestException;
import org.example.librarymanagement.exception.exception.ExpiredJwtException;
import org.example.librarymanagement.service.SessionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@RequiredArgsConstructor
@Service
public class JwtService {

    @Value("${secret-key}")
    private String SECRET_KEY;

    @Value("#{1000 * 60 * 20}")
    private int ACCESS_TOKEN_EXPIRATION_TIME; // Access token expires in 5 minutes

    @Value("#{1000 * 60 * 60 * 24 * 7}")
    private int REFRESH_TOKEN_EXPIRATION_TIME; // Refresh token expires in 7 days

    private final ResourceBundle resourceBundle;

    public String extractJwtToken(String authHeader){
        return authHeader.substring(7);
    }

    public String extractEmail(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public String extractJti(String token){
        return extractClaim(token, Claims::getId);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimResolver){
        final Claims claims = extractAllClaim(token);
        return claimResolver.apply(claims);
    }

    public Claims extractAllClaim(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSigninKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateToken(UserDetails userDetails, String jti){
        return generateToken(new HashMap<>(), userDetails, jti, ACCESS_TOKEN_EXPIRATION_TIME);
    }

    public String generateRefreshToken(UserDetails userDetails, String jti){
        return generateToken(new HashMap<>(), userDetails, jti, REFRESH_TOKEN_EXPIRATION_TIME);
    }

    public String generateRefreshToken(UserDetails userDetails, String jti, Date issuedTime, Date expirationTime){
        return generateToken(new HashMap<>(), userDetails, jti, issuedTime, expirationTime);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails, String jti, Date issuedTime, Date expirationTime){
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setId(jti)
                .setIssuedAt(issuedTime)
                .setExpiration(expirationTime)
                .signWith(getSigninKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails, String jti, long expirationTime){

        Date current = new Date(System.currentTimeMillis());
        Date expiration = new Date(current.getTime() + expirationTime);

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setId(jti)
                .setIssuedAt(current)
                .setExpiration(expiration)
                .signWith(getSigninKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateJti(){
        return UUID.randomUUID().toString();
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String email = extractEmail(token);
        return email.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        boolean isExpired = extractExpiration(token).before(new Date());
        if(isExpired){
            throw new ExpiredJwtException(
                    resourceBundle.getString("session.jwt.jti-is-expired"),
                    "session.jwt.jti-is-expired");
        }
        return false;
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Date extractIssueAt(String token){
        return extractClaim(token, Claims::getIssuedAt);
    }

    private Key getSigninKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public static void validateRefreshToken(String authorization, JwtService jwtService, SessionService sessionService, ResourceBundle resourceBundle){
        String refreshToken = jwtService.extractJwtToken(authorization);
        String jti = jwtService.extractJti(refreshToken);
        Session session = sessionService.getSessionWithJti(jti);

        if(session.getExpirationDate().getTime() > jwtService.extractExpiration(refreshToken).getTime()){
            throw new BadRequestException("session.jti.jti-not-valid",
                    resourceBundle.getString("session.jti.jti-not-valid"));
        }

        if (!session.isActive()){
            throw new ExpiredJwtException("session.jti.jti-not-active",
                    resourceBundle.getString("session.jti.jti-not-active"));
        }
    }

    public static void checkJti(String authorization, JwtService jwtService, SessionService sessionService, ResourceBundle resourceBundle) {
        String refreshToken = jwtService.extractJwtToken(authorization);
        String jti = jwtService.extractJti(refreshToken);
        Session session = sessionService.getSessionWithJti(jti);

        if(session.getExpirationDate().before(new Date())){
            sessionService.deactivateSession();
            throw new ExpiredJwtException("session.jti.jti-expired",
                    resourceBundle.getString("session.jti.jti-expired"));
        }

        if (!session.isActive()){
            throw new ExpiredJwtException("session.jti.jti-not-active",
                    resourceBundle.getString("session.jti.jti-not-active"));
        }
    }
}
