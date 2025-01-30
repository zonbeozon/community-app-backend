package com.zonbeozon.communityapp.crpyto.exception;

import com.zonbeozon.communityapp.exception.CustomException;
import com.zonbeozon.communityapp.exception.ErrorCode;

public class FetchException extends CustomException {
    public FetchException(ErrorCode errorCode) {
        super(errorCode);
    }
}
