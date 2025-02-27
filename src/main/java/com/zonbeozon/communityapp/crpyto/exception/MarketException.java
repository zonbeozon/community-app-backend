package com.zonbeozon.communityapp.crpyto.exception;

import com.zonbeozon.communityapp.exception.CustomException;
import com.zonbeozon.communityapp.exception.ErrorCode;

public class MarketException extends CustomException {
  public MarketException(ErrorCode errorCode) {
    super(errorCode);
  }
}
