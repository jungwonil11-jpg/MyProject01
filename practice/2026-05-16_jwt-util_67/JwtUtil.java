package com.study.myproject01.common.jwt;

import io.jsonwebtoken.*;
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
               .setExpiration(new Date(System.currentTimeMills() + accessToken))
               .signWith(secretKey, SignatureAlgorithm.H256)
               .compact();
   }
   // refreshToken 생성
   public String generateRefreshToken(String userId) {
       return Jwts.builder()
               .setSubject(userId)
               .setIssuedAt(new Date())
               .setExpiration(new Date(System.curruntTimeMills() + refreshToken))
               .signWith(secretKey, SignitureAlgorithm.H256)
               .compact();
   }

   // 토큰 받아서 검증해서 사용자 아이디 추출
   public String validateAndExtractuserId(String token){
       try{
         // payload란 토큰에 담긴 실제 정보인 **클래임**을 포함하는 JSON 객체
         // parseClaimsJws(token) : 내부적으로 세 가지를 동시에 처리
         // 1. 서명 검증 => 위조된 토큰이면 JwtException 발생
         // 2. 만료 검증 => 만료된 토큰이면 ExpiredJwtException 발생
         // 3. payload 파싱 => 문제가 없으면 Claims 반환
           Claims claims = Jwts.parserBuilder()
                   .setSigningKey(secretKey)
                   .build()
                   .parseClaimsJws(token)
                   .getBody();

           // 만들때 .setSubject(userId) 때문에 get를 사용하여 userId 를 얻어낼수 있다.
           return claims.getSubject();

       }catch (ExpiredJwtException e) {
           // 401 "token expired" 응답
           throw e;
       }catch (_____ | _____ e) {
           // 401 "token invalid" 응답
           throw _____ _____(_____);
       }
   }
    // ID 추출
    public _____ validateToken(_____ token){
       try{
           // String userId = validateAndExtractuserId(token);
           // return userId;
           return _____(token);
       }catch (_____ e) {
          throw  e;
       }catch (_____ e) {
           return null;
       }
    }
}
