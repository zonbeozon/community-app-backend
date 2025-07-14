package com.zonbeozon.global.exception;

public class BadRequestException extends ZonbeozonException {
    public BadRequestException(ErrorCode errorCode) {
        super(errorCode);
    }
}
