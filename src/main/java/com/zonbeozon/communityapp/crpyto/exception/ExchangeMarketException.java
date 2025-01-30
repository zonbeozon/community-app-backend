package com.zonbeozon.communityapp.crpyto.exception;

import com.zonbeozon.communityapp.exception.CustomException;
import com.zonbeozon.communityapp.exception.ErrorCode;

public class ExchangeMarketException extends CustomException {
  public ExchangeMarketException(ErrorCode errorCode) {
    super(errorCode);
  }
}
