package com.zonbeozon.global.exception;

public class NotFoundException extends ZonbeozonException {
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
