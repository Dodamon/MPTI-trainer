package mpti.common.security;

import lombok.RequiredArgsConstructor;
import mpti.domain.trainer.application.TrainerAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private TrainerAuthService trainerAuthService;

    private static final Logger logger = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String accessToken = getJwtFromRequest(request);
            String refreshToken = getRefreshJwtFromRequest(request);

            if (StringUtils.hasText(accessToken) && tokenProvider.validateToken(accessToken)) {
                String userEmail = tokenProvider.getUserIdFromToken(accessToken);

                UserDetails userDetails = customUserDetailsService.loadUserByUsername(userEmail);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);

            } else if( tokenProvider.isExpiredToken(accessToken)) {
                logger.info("Access token is expired");
                if(tokenProvider.validateToken(refreshToken)) {
                    // Redis DB에 확인
                    if(!trainerAuthService.isValidDB(refreshToken)) {
                        throw new RuntimeException("Refresh token is not in DB");
                    } else {
                        // Redis DB 확인 완료 후 access token 재발급

                        String userEmail = tokenProvider.getUserIdFromToken(refreshToken);
                        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userEmail);
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);

                        accessToken = tokenProvider.createAccessToken(authentication);

                        response.setHeader("Authorization", "Bearer " + accessToken);
                    }

                } else {
                    throw new RuntimeException("Refresh token is not valid");
                }
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
            throw new RuntimeException(ex);
        }

        logger.info("토큰 검사 완료");
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

    private String getRefreshJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Refresh-token");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
}
