package com.zonbeozon.communityapp.exchangerate.exception;

import com.zonbeozon.communityapp.exception.CustomException;
import com.zonbeozon.communityapp.exception.ErrorCode;

public class ExchangeRateException extends CustomException {
    public ExchangeRateException(ErrorCode errorCode) {
        super(errorCode);
    }
}
