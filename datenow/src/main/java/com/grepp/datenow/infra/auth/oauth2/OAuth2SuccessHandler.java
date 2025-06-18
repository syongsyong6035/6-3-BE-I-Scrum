package com.grepp.datenow.infra.auth.oauth2;

import com.grepp.datenow.app.model.auth.domain.Principal;
import com.grepp.datenow.app.model.auth.service.AuthService;
import com.grepp.datenow.app.model.auth.token.dto.TokenDto;
import com.grepp.datenow.app.model.member.entity.Member;
import com.grepp.datenow.app.model.member.repository.MemberRepository;
import com.grepp.datenow.infra.auth.oauth2.user.OAuth2UserInfo;
import com.grepp.datenow.infra.auth.token.TokenCookieFactory;
import com.grepp.datenow.infra.auth.token.code.TokenType;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    
    private final MemberRepository memberRepository;
    private final AuthService authService;
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException {

        OAuth2User user = (OAuth2User) authentication.getPrincipal();
        OAuth2UserInfo userInfo = OAuth2UserInfo.create(request.getRequestURI(), user);

        String userId = userInfo.getProvider() + "_" + userInfo.getProviderId();
        Optional<Member> optionalMember = memberRepository.findByUserId(userId);

        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();

            List<SimpleGrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_USER")
            );

            Principal principal = Principal.createPrincipal(member, authorities);

            UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(principal, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(auth);

            TokenDto dto = authService.processTokenSignin(userId);

            ResponseCookie accessTokenCookie = TokenCookieFactory.create(TokenType.ACCESS_TOKEN.name(),
                dto.getAccessToken(), dto.getAtExpiresIn());
            ResponseCookie refreshTokenCookie = TokenCookieFactory.create(TokenType.REFRESH_TOKEN.name(),
                dto.getRefreshToken(), dto.getRtExpiresIn());

            response.addHeader("Set-Cookie", accessTokenCookie.toString());
            response.addHeader("Set-Cookie", refreshTokenCookie.toString());

            response.sendRedirect("/");
        } else {
            HttpSession session = request.getSession();
            session.setAttribute("oauth2_user_info", userInfo);

            response.sendRedirect("/member/oauth/signup");
        }
    }
}
