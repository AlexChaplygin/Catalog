package ru.reksoft.chaplygin.catalog.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Service;
import ru.reksoft.chaplygin.catalog.configuration.Constants;
import ru.reksoft.chaplygin.catalog.dto.TokenObjectDTO;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;

/**
 * Service to work with tokens.
 */
@Service
public class TokenService {

    public TokenObjectDTO createToken(String userName) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(Constants.TOKEN_SECRET);

            Calendar c = Calendar.getInstance();
            c.add(Calendar.MINUTE, Constants.EXPIRATION_MIN);
            Date expirationDate = c.getTime();

            String token = JWT.create()
                    .withClaim("userName", userName)
                    .withClaim("createdAt", new Date())
                    .withExpiresAt(expirationDate)
                    .sign(algorithm);
            return new TokenObjectDTO(token);
        } catch (UnsupportedEncodingException exception) {
            exception.printStackTrace();

        } catch (JWTCreationException exception) {
            exception.printStackTrace();

        }
        return null;
    }

    public String getUserNameFromToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(Constants.TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getClaim("userName").asString();
        } catch (UnsupportedEncodingException exception) {
            exception.printStackTrace();

            return null;
        } catch (JWTVerificationException exception) {
            exception.printStackTrace();

            return null;
        }
    }

    public boolean isTokenValid(String token) {
        String userId = this.getUserNameFromToken(token);
        return userId != null;
    }
}