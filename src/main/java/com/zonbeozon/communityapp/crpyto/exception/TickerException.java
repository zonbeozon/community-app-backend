package com.zonbeozon.communityapp.crpyto.exception;

import com.zonbeozon.communityapp.exception.CustomException;
import com.zonbeozon.communityapp.exception.ErrorCode;

public class TickerException extends CustomException {
  public TickerException(ErrorCode errorCode) {
    super(errorCode);
  }
}
