package com.zonbeozon.global.exception;

public class AccessDeniedException extends ZonbeozonException {
    public AccessDeniedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
