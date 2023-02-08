package mpti.common.security;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.stream.Collectors;

@Service
public class TokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

    private static final String AUTHORITIES_KEY = "auth";

    @Value("${app.auth.tokenSecret:}")
    private String SECRET_KEY;

    @Value("${app.auth.accessTokenExpirationMsec}")
    private long ACCESS_TOKEN_EXPIRATION;


    public String getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        logger.info("authentication");
        logger.info(claims.getSubject());
        logger.info(claims.getId());

        return claims.getSubject();
    }

    public boolean validateToken(String authToken) {
        logger.info("Custom validateToken filter start");
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(authToken);
            logger.info("Valid JWT access token");
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT access signature"); //"Invalid JWT signature"
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT access token"); // Invalid JWT token
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT access token"); // Expired JWT token
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT access token"); // Unsupported JWT token
        } catch (IllegalArgumentException ex) {
            logger.error("JWT access claims string is empty"); // JWT claims string is empty.
        }
        return false;
    }

    public boolean isExpiredToken(String authToken) {

        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(authToken);
            return true;
        } catch (ExpiredJwtException ex) {
            logger.error("Expired Refresh JWT token");
            return true;
        } catch (Exception ex) {
            logger.error("JWT refresh token error");
        }

        return false;
    }

    public String createAccessToken(Authentication authentication) {

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + ACCESS_TOKEN_EXPIRATION);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(authentication.getName())
                .setId(userPrincipal.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }
}
