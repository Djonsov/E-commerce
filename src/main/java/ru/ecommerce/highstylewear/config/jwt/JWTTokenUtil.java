package ru.ecommerce.highstylewear.config.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

@Component
@Slf4j
public class JWTTokenUtil {

    //7*24*60*1000 = 1 week in millis - время жизни токена
    public static final long JWT_TOKEN_VALIDITY = 604800000;
    private final String secret = "secret";

    private static  final ObjectMapper objectMapper = getDefaultObjectMapper();

    private static ObjectMapper getDefaultObjectMapper() {
        return new ObjectMapper();
    }

    public String generateToken(final UserDetails userDetails){
        return doGenerateToken(userDetails.toString());
    }

    //Генерация токена
    private String doGenerateToken(final String payload){
        log.info(payload);
        return Jwts.builder()
                .setSubject(payload)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ JWT_TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    //проверка актуальности токена
    private Boolean isTokenExpired(final String token){
        final Date expiration = getExpirationDateFromTake(token);
        return expiration.before(new Date());
    }

    private Date getExpirationDateFromTake(String token) {
        //нужно достать expirationDate из токена
        return getClaimsFromToken(token, Claims::getExpiration);
    }

    public Boolean validateToken(final String token, UserDetails userDetails){
        final String userName = getUsernameFromToken(token);

        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String getUsernameFromToken(final String token) {
        String claims = getClaimsFromToken(token, Claims::getSubject);
        JsonNode claimJSON = null;
        try {
            claimJSON = objectMapper.readTree(claims);
        } catch (JsonProcessingException e) {
            log.error("JWTTokenUtil#getUserNameFromToken(): {}", e.getMessage());
        }

        if(claimJSON != null){
            return claimJSON.get("username").asText();
        }else
            return null;
    }

    public String getRoleFromToken(final String token) {
        String claims = getClaimsFromToken(token, Claims::getSubject);
        JsonNode claimJSON = null;
        try {
            claimJSON = objectMapper.readTree(claims);
        } catch (JsonProcessingException e) {
            log.error("JWTTokenUtil#getRoleFromToken(): {}", e.getMessage());
        }

        if(claimJSON != null){
            return claimJSON.get("user_role").asText();
        }else
            return null;
    }

    private <T> T getClaimsFromToken(final String token, Function<Claims, T> claimResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(final String token){
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }
}
