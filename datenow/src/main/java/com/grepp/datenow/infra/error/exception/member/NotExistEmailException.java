package com.grepp.datenow.infra.error.exception.member;

import com.grepp.datenow.infra.error.exception.CommonException;
import com.grepp.datenow.infra.response.ResponseCode;

public class NotExistEmailException extends CommonException {

    public NotExistEmailException() {
        super(ResponseCode.BAD_REQUEST, "존재하지 않는 메일입니다.");
    }
}
