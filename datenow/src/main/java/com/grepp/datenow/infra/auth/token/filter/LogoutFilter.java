package com.grepp.datenow.infra.auth.token.filter;

import com.grepp.datenow.app.model.auth.token.RefreshTokenService;
import com.grepp.datenow.infra.auth.token.JwtProvider;
import com.grepp.datenow.infra.auth.token.TokenCookieFactory;
import com.grepp.datenow.infra.auth.token.code.TokenType;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class LogoutFilter extends OncePerRequestFilter {
    
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        
        String accessToken = jwtProvider.resolveToken(request, TokenType.ACCESS_TOKEN);
        
        if(accessToken == null){
            filterChain.doFilter(request,response);
            return;
        }
        
        String path = request.getRequestURI();
        Claims claims  = jwtProvider.parseClaim(accessToken);
        
        if(path.equals("/auth/logout")){
            // accesstoken에서 id 추출 후 redis에서 refreshtoken 삭제
            refreshTokenService.deleteByAccessTokenId(claims.getId());

            // 각 쿠키들 만료 설정
            ResponseCookie expiredAccessToken = TokenCookieFactory.createExpiredToken(TokenType.ACCESS_TOKEN);
            ResponseCookie expiredRefreshToken = TokenCookieFactory.createExpiredToken(TokenType.REFRESH_TOKEN);
            ResponseCookie expiredSessionId = TokenCookieFactory.createExpiredToken(TokenType.AUTH_SERVER_SESSION_ID);
            response.addHeader("Set-Cookie", expiredAccessToken.toString());
            response.addHeader("Set-Cookie", expiredRefreshToken.toString());
            response.addHeader("Set-Cookie", expiredSessionId.toString());

            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        
        filterChain.doFilter(request,response);
    }
}
