package com.grepp.datenow.infra.error;

import com.grepp.datenow.infra.error.exception.member.NotExistEmailException;
import com.grepp.datenow.infra.error.exception.member.SessionExpiredException;
import com.grepp.datenow.infra.error.exception.member.TokenMismatchException;
import java.net.URLEncoder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackages = "com.grepp.datenow.app.controller.web")
public class WebExceptionAdvice {

    // 여기부터 Signup 인증 관련
    // 한글 넣으니깐 깨져서 인코딩 해야함 ;;
    @ExceptionHandler(TokenMismatchException.class)
    public String TokenMismatchExceptionHandler(TokenMismatchException ex) {
        String encodedMessage = URLEncoder.encode(ex.getMessage());
        return "redirect:/member/signin?error=" + encodedMessage;
    }

    @ExceptionHandler(SessionExpiredException.class)
    public String SessionExpiredExceptionHandler(SessionExpiredException ex) {
        String encodedMessage = URLEncoder.encode(ex.getMessage());
        return "redirect:/member/signin?error=" + encodedMessage;
    }
    // 여기까지 Signup 인증 관련

    // 비밀번호 찾기 관련
    // 존재하지 않는 메일 입력 시
    @ExceptionHandler(NotExistEmailException.class)
    public String NotExistEmailExceptionHandler(NotExistEmailException ex) {
        String encodedMessage = URLEncoder.encode(ex.getMessage());
        return "redirect:/member/signin?error=" + encodedMessage;
    }
}
