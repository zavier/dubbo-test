package com.zavier.dubbotest.service.login;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.zavier.dubbotest.common.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * The type Token service.
 *
 * @date 2020-01-15 12:44
 * @author zhengwei20
 */
@Slf4j
@Service
public class TokenService {
    /**
     * The Secret.
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * The Issuer.
     */
    private String issuer = "dubbo-test";

    /**
     * Check token boolean.
     *
     * @param token the token
     * @return the boolean
     */
    public boolean checkToken(String token) {
        if (StringUtils.isBlank(token)) {
            return false;
        }
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
        } catch (
                JWTVerificationException exception) {
            return false;
        }
        return true;
    }

    /**
     * Generate token string.
     *
     * @param subject the subject
     * @return the string
     */
    public String generateToken(String subject) {
        // 2天有效
        long expireTime = System.currentTimeMillis() + 2 * 24 * 3600 * 1000;
        Date date = new Date(expireTime);
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer(issuer)
                    .withSubject(subject)
                    .withExpiresAt(date)
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception){
            log.error("generateToken error", exception);
            throw new BusinessException("generateToken error");
        }
    }

    /**
     * Gets token from header.
     *
     * @param authHeader the auth header
     * @return the token from header
     */
    public String getTokenFromHeader(String authHeader) {
        if (StringUtils.isBlank(authHeader)) {
            return "";
        }
        return authHeader.substring(authHeader.indexOf(" ") + 1)
;    }
}
