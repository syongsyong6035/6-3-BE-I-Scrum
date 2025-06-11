package com.grepp.datenow.infra.config;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Value("${remember-me.key}")
    private String rememberMeKey;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
            .build();
    }

    @Bean
    public AuthenticationFailureHandler failureHandler() {
        return new AuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest request,
                HttpServletResponse response, AuthenticationException exception)
                throws IOException, ServletException {

                String errorMessage = "아이디 또는 비밀번호가 잘못되었습니다.";

                if (exception instanceof InternalAuthenticationServiceException &&
                    exception.getCause() instanceof DisabledException) {
                    errorMessage = "탈퇴된 회원입니다.";
                }

                response.sendRedirect("/member/signin?error=" + URLEncoder.encode(errorMessage,
                    StandardCharsets.UTF_8));
            }
        };
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request,
                HttpServletResponse response, Authentication authentication)
                throws IOException, ServletException {

                boolean isAdmin = authentication.getAuthorities()
                    .stream()
                    .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_ADMIN"));

                if (isAdmin) {
                    response.sendRedirect("/");
                    return;
                }

                response.sendRedirect("/");
            }
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // * : 1depth 아래 모든 경로
        // ** : 모든 depth 의 모든 경로
        // Security Config 에는 인증과 관련된 설정만 지정 (PermitAll or Authenticated)
        http
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/api/**")  // REST API는 제외
            )
            .authorizeHttpRequests(
                (requests) -> requests
                    .requestMatchers(GET, "/css/**", "/js/**", "/images/**").permitAll()
                    .requestMatchers(GET, "/member/signup", "/member/signup/**", "/member/signin",
                        "/member/find-password").permitAll()
                    .requestMatchers(POST, "/member/signin", "/member/signup",
                        "/member/find-password").permitAll()
                    .requestMatchers("/api/members/exists", "/api/members/check/email",
                        "/api/members/check/nickname", "/api/members/signup").permitAll()
                    .requestMatchers(
                        "/v3/api-docs/**",
                        "/swagger-ui.html",
                        "/swagger-ui/**"
                    ).permitAll()
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    .requestMatchers("/api/**").authenticated()
                    .anyRequest().authenticated()
            )
            .formLogin((form) -> form
                .loginPage("/member/signin")
                .usernameParameter("userId")
                .loginProcessingUrl("/member/signin")
                .defaultSuccessUrl("/")
                .successHandler(successHandler())
                .failureHandler(failureHandler())
                .permitAll()
            )
            .rememberMe(rememberMe -> rememberMe.key(rememberMeKey))
            .logout(LogoutConfigurer::permitAll);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
