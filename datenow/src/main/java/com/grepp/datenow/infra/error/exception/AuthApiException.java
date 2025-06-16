package com.grepp.datenow.infra.error.exception;

import com.grepp.datenow.infra.response.ResponseCode;

public class AuthApiException extends CommonException {
    
    public AuthApiException(ResponseCode code) {
        super(code);
    }
    
    public AuthApiException(ResponseCode code, Exception e) {
        super(code, e);
    }
}
