package com.grepp.datenow.infra.response;

import org.springframework.http.HttpStatus;

public enum ResponseCode {
    OK("0000", HttpStatus.OK, "정상적으로 완료되었습니다."),
    BAD_REQUEST("4000", HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    UNAUTHORIZED("4010", HttpStatus.UNAUTHORIZED, "권한이 없습니다."),
    BAD_CREDENTIAL("4011", HttpStatus.UNAUTHORIZED, "아이디나 비밀번호가 틀렸습니다."),
    INVALID_TOKEN("4002", HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    NOT_FOUND("4004", HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다."),
    CONFLICT("4009", HttpStatus.CONFLICT, "이미 등록된 코스입니다."),
    PAYLOAD_TOO_LARGE("4013", HttpStatus.PAYLOAD_TOO_LARGE, "요청된 이미지의 크기가 너무 큽니다."), // HTTP 413
    BAD_WORD("4005", HttpStatus.BAD_REQUEST, "비속어가 감지되었습니다."),

    INTERNAL_SERVER_ERROR("5000", HttpStatus.INTERNAL_SERVER_ERROR, "서버에러 입니다."),
    SECURITY_INCIDENT("6000", HttpStatus.UNAUTHORIZED, "계정에 비정상적인 접근이 감지되었습니다.");

    private final String code;
    private final HttpStatus status;
    private final String message;
    
    ResponseCode(String code, HttpStatus status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }
    
    public String code() {
        return code;
    }
    
    public HttpStatus status() {
        return status;
    }
    
    public String message() {
        return message;
    }
}
