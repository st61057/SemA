//package org.example.config;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.ExpiredJwtException;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//import java.util.function.Function;
//
//@Component
//public class JwtTokenUtil {
//    private static final String SECRET_KEY = "mysecretkey";
//    private static final long EXPIRATION_TIME = 30000;
//
//    // Generování tokenu s expirací
//    public String generateToken(String username) {
//        return Jwts.builder()
//                .setSubject(username)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
//                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
//                .compact();
//    }
//
//    public Claims getClaimsFromToken(String token) {
//        try {
//            return Jwts.parser()
//                    .setSigningKey(SECRET_KEY)
//                    .parseClaimsJws(token)
//                    .getBody();
//        } catch (Exception e) {
//            return null;
//        }
//    }
//
//
//    public boolean isTokenValid(String token) {
//        Claims claims = getClaimsFromToken(token);
//        return (claims != null && claims.getExpiration().after(new Date()));
//    }
//
//    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
//        final Claims claims = getClaimsFromToken(token);
//        return claimsResolver.apply(claims);
//    }
//}
