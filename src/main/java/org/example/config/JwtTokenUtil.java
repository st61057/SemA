package org.example.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtTokenUtil {
    private static final String SECRET_KEY = "652846d3c58093f95f77614c48ac79e005051f6cf49b8ef45081b91518785c7d07ea03f51e141ca0399ff146cab6ea545d20b2f0e97d89da19eb1c00eaad07c24f71846bb7c91b2d495767dc46f4daada6714481346cc81a3a01aabdd8e73c72710ee273383719fb2c602154692d727713fec412252cbd39cd69ab39118b1d865aaff7040ce0cb3f65097203b37f18fdac2f86a7c018a374c5c453b5281e59780209d1d28566cc04b91773d22b7397201acd7626831094c0591cd2cf8cc1acefb6056d644802d201b798df8b6a8b78aa7ad8668cb0f27af5958c0696382d0daadbfb2b32b64f10f1255ad83c80335ac1d92e64e09f05fe6c0644557c291c3d8d2997435c8fe6e89d67e6cbf923548c5ff01003f377d745ab9e5185fa7fdc609119c5fb0eb693d9a582e7e7df551577c4ac88edd4ff499aa74da1a5cbfc0f70d38ea94de188c2d1247526e506d73cb5ca894e17f83f31d834fa5b6d071adb1c090d596e061b9fe9e3bd9e094103e1b7013481c8c21f5c9ef68e7e4bb59df72dc00bc6c3c140e13c33d40d7c1552c11dd99c34fde4024888674091fad76062e16ac6bc536fe3260182a159046e2fd5e61f087d134f72c27fb168cb32b56c35256aaea18495880563a0ac3f69098a79798ca5c675fbf55a1a8064e18e4cdb0ccb2dbbd00a28dc3d0a2ede13c3f77aeee3b840f3716f13a3ade4215a88d9be79b068";
    private static final long EXPIRATION_TIME = 30000;

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isTokenValid(String token) {
        Claims claims = getClaimsFromToken(token);
        return (claims != null && claims.getExpiration().after(new Date()));
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
    }
}
