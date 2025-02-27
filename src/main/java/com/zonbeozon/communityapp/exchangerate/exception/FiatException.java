package com.zonbeozon.communityapp.exchangerate.exception;

import com.zonbeozon.communityapp.exception.CustomException;
import com.zonbeozon.communityapp.exception.ErrorCode;

public class FiatException extends CustomException {
  public FiatException(ErrorCode errorCode) {
    super(errorCode);
  }
}
