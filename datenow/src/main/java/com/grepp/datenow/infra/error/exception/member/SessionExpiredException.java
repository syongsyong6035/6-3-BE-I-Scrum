package com.grepp.datenow.infra.error.exception.member;

public class SessionExpiredException extends RuntimeException {
    public SessionExpiredException(String message) {
        super(message);
    }
}
