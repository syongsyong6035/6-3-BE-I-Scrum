package com.grepp.datenow.app.model.auth.service;

import com.grepp.datenow.app.controller.api.auth.payload.SigninRequest;
import com.grepp.datenow.app.model.auth.token.RefreshTokenRepository;
import com.grepp.datenow.app.model.auth.token.RefreshTokenService;
import com.grepp.datenow.app.model.auth.token.UserBlackListRepository;
import com.grepp.datenow.app.model.auth.token.dto.AccessTokenDto;
import com.grepp.datenow.app.model.auth.token.dto.TokenDto;
import com.grepp.datenow.app.model.auth.token.entity.RefreshToken;
import com.grepp.datenow.infra.auth.token.JwtProvider;
import com.grepp.datenow.infra.auth.token.code.GrantType;
import com.grepp.datenow.infra.error.exception.CommonException;
import com.grepp.datenow.infra.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AuthService {

    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserBlackListRepository userBlackListRepository;
    private final JwtProvider jwtProvider;

    public TokenDto signin(SigninRequest loginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword());

        // loadUserByUsername + password 검증 후 인증 객체 반환
        // 인증 실패 시: AuthenticationException 발생
        try {
            Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(authenticationToken);

            SecurityContextHolder.getContext().setAuthentication(authentication);
            return processTokenSignin(authentication.getName());
        } catch (AuthenticationException e) {
            throw new CommonException(ResponseCode.BAD_CREDENTIAL, e);
        }
    }

    public TokenDto processTokenSignin(String userId){
        // black list 에 있다면 해제
        userBlackListRepository.deleteById(userId);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        AccessTokenDto accessToken = jwtProvider.generateAccessToken(userId);
        RefreshToken refreshToken = refreshTokenService.saveWithAtId(accessToken.getId());

        return TokenDto.builder()
            .accessToken(accessToken.getToken())
            .refreshToken(refreshToken.getToken())
            .grantType(GrantType.BEARER)
            .atExpiresIn(jwtProvider.getAtExpiration())
            .rtExpiresIn(jwtProvider.getRtExpiration())
            .build();
    }

}
