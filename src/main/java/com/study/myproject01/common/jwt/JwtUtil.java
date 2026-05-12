package com.study.myproject01.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;

public class JwtUtil {
    private final Key secretKey;
    private  long accessToken;
    private  long refreshToken;

    // 생성자
   public JwtUtil(String secret, long accessToken, long refreshToken) {
      this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
      this.accessToken = accessToken;
      this.refreshToken = refreshToken;
   }

   // accessToken 생성
   public String generateAccessToken(String userId) {
       return Jwts.builder()
               .setSubject(userId)
               .setIssuedAt(new Date())
               .setExpiration(new Date(System.currentTimeMillis() + accessToken))
               .signWith(secretKey, SignatureAlgorithm.HS256)
               .compact();
   }
   // refreshToken 생성
   public String generateRefreshToken(String userId) {
       return Jwts.builder()
               .setSubject(userId)
               .setIssuedAt(new Date())
               .setExpiration(new Date(System.currentTimeMillis() + refreshToken))
               .signWith(secretKey, SignatureAlgorithm.HS256)
               .compact();
   }
   // 토큰 받아서 검증해서 사용자 아이디 추출
   public String validateAndExtractuserId(String token){
       try{
         // 넘어온 token "Bearer Token내용"
         // Bearer 는 HTTP Authorization 헤더를 통해서 토큰 전달 방식
         // token = token.substring(7)

          // 페이로드란 토큰에 담기 실제 정보인 **클래임**을 포함하는 JSON 객체
           Claims claims = Jwts.parserBuilder()
                   .setSigningKey(secretKey)
                   .build()
                   .parseClaimsJws(token)
                   .getBody();

           // 만들때 .setSubject(userId) 때문에 get를 사용하여 userId 를 얻어낼수 있다.
           return claims.getSubject();

       } catch (JwtException | IllegalArgumentException e) {
           throw new IllegalArgumentException("Token Error");
       }
   }
    // ID 추출
    public String validateToken(String token){
       try{
           String userId = validateAndExtractuserId(token);
           return userId;
       } catch (Exception e) {
           return null;
       }
    }
    
    // 만료날짜 추출 메서드 
    public Date extractExpiration(String token){
       return Jwts.parserBuilder()
               .setSigningKey(secretKey)
               .build()
               .parseClaimsJws(token)
               .getBody()
               .getExpiration();
    }
    
    // 만료 확인 메서드 
    public boolean isTokenExpired(String token){
       // 만료시간 추출
       Date expiration = extractExpiration(token);
       return expiration.before(new Date()); // 현재 시간 보다 이전 이면 만료
    }













}
