package com.grepp.datenow.infra.error.exception;

import com.grepp.datenow.infra.response.ResponseCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommonException extends RuntimeException {

    private final ResponseCode code;
    private final String customMessage;

    public CommonException(ResponseCode code) {
        this.code = code;
        this.customMessage = code.message();
    }

    public CommonException(ResponseCode code, String customMessage) {
        this.code = code;
        this.customMessage = customMessage;
    }

    public CommonException(ResponseCode code, Exception e) {
        super(e);
        this.code = code;
        this.customMessage = code.message();
        log.error(e.getMessage(), e);
    }

    public ResponseCode code() {
        return code;
    }

    @Override
    public String getMessage() {
        return customMessage;
    }
}