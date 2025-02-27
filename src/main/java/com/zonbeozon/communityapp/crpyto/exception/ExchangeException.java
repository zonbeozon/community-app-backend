package com.zonbeozon.communityapp.crpyto.exception;

import com.zonbeozon.communityapp.exception.CustomException;
import com.zonbeozon.communityapp.exception.ErrorCode;

public class ExchangeException extends CustomException {
    public ExchangeException(ErrorCode errorCode) {
        super(errorCode);
    }
}
