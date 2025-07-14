package com.zonbeozon.global.exception;

public class ConflictException extends ZonbeozonException {
    public ConflictException(ErrorCode errorCode) {
        super(errorCode);
    }
}
