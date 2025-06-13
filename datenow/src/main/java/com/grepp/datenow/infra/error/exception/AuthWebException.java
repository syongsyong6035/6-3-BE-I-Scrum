package com.grepp.datenow.infra.error.exception;

import com.grepp.datenow.infra.error.CommonException;
import com.grepp.datenow.infra.response.ResponseCode;

public class AuthWebException extends CommonException {
    
    public AuthWebException(ResponseCode code) {
        super(code);
    }
    
    public AuthWebException(ResponseCode code, String redirect) {
        super(code, redirect);
    }
    
    public AuthWebException(ResponseCode code, Exception e) {
        super(code, e);
    }
}
