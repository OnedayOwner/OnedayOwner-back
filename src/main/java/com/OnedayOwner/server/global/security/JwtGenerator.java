package com.OnedayOwner.server.global.security;

import com.OnedayOwner.server.global.exception.BusinessException;
import com.OnedayOwner.server.global.exception.ErrorCode;
import com.OnedayOwner.server.platform.user.entity.Customer;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.RSAKeyProvider;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtGenerator {
    private final JwtConfig jwtConfig;
    private final RSAKeyProvider rsaKeyProvider;

    public String generate(Long subjectId, List<String> roles, OffsetDateTime now) {

        long nowMils = now.toInstant().toEpochMilli();
        long expiredIn = nowMils + jwtConfig.getExpiration() * 1000L;

        String token = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "RS256")
                .setIssuer("newsboy")
                .setSubject(subjectId.toString())
                .setExpiration(new Date(expiredIn))
                .setIssuedAt(new Date(nowMils))
                .claim("authorities", roles)
                .claim("type", "accessToken")
                .signWith(SignatureAlgorithm.valueOf("RS256"), rsaKeyProvider.getPrivateKey())
                .compact();
        return token;
    }

    public void verify(String token, Customer user){

        Algorithm algorithm = Algorithm.RSA256(rsaKeyProvider);
        algorithm.verify(JWT.decode(token));
        DecodedJWT decodedJWT = JWT.decode(token);
        String userId = decodedJWT.getSubject();

        if(!user.getId().equals(Long.parseLong(userId))){ // 토큰을 해석해서 얻은 사용자와 accessToken에 저장된 사용자가 일치하지 않으면
            throw new BusinessException(ErrorCode.TOKEN_USER_NOT_MATCH);
        }

    }
}
