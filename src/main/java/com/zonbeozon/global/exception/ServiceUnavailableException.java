package com.zonbeozon.global.exception;

public class ServiceUnavailableException extends ZonbeozonException {
    public ServiceUnavailableException(ErrorCode errorCode) {
        super(errorCode);
    }
}
