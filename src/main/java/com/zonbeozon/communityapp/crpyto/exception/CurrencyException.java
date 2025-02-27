package com.zonbeozon.communityapp.crpyto.exception;

import com.zonbeozon.communityapp.exception.CustomException;
import com.zonbeozon.communityapp.exception.ErrorCode;

public class CurrencyException extends CustomException {
    public CurrencyException(ErrorCode errorCode) {
        super(errorCode);
    }
}
