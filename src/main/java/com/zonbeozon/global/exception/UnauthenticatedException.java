package com.zonbeozon.global.exception;

public class UnauthenticatedException extends ZonbeozonException {
    public UnauthenticatedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
