package com.uisrael.hikvisionadmin.infrastructure.security;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

  @Value("${jwt.secret:miClaveSecretaMuyLargaParaJWTQueDebeTenerAlMenos256BitsDeSeguridad}")
  private String secretKey;

  @Value("${jwt.expiration:86400000}")
  private Long jwtExpiration;

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public Long extractAdminId(String token) {
    return extractClaim(token, claims -> claims.get("adminId", Long.class));
  }

  @SuppressWarnings("unchecked")
  public List<Long> extractBuildingIds(String token) {
    return extractClaim(token, claims -> claims.get("buildingIds", List.class));
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  public String generateToken(String username, Long adminId, List<Long> buildingIds) {
    Map<String, Object> extraClaims = new HashMap<>();
    extraClaims.put("adminId", adminId);
    extraClaims.put("buildingIds", buildingIds);
    return generateToken(extraClaims, username);
  }

  public String generateToken(Map<String, Object> extraClaims, String username) {
    return buildToken(extraClaims, username, jwtExpiration);
  }

  public Long getExpirationTime() {
    return jwtExpiration;
  }

  private String buildToken(Map<String, Object> extraClaims, String username, Long expiration) {
    return Jwts.builder()
        .claims(extraClaims)
        .subject(username)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + expiration))
        .signWith(getSignInKey(), Jwts.SIG.HS256)
        .compact();
  }

  public boolean isTokenValid(String token, String username) {
    final String tokenUsername = extractUsername(token);
    return (tokenUsername.equals(username)) && !isTokenExpired(token);
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser()
        .verifyWith(getSignInKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  private SecretKey getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }

}
