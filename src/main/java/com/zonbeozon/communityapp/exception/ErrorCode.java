package com.zonbeozon.communityapp.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // auth
    ILLEGAL_REGISTRATION_ID(NOT_ACCEPTABLE, "illegal registration id"),
    TOKEN_EXPIRED(UNAUTHORIZED, "토큰이 만료되었습니다."),
    INVALID_TOKEN(UNAUTHORIZED, "올바르지 않은 토큰입니다."),
    INVALID_JWT_SIGNATURE(UNAUTHORIZED, "잘못된 JWT 시그니처입니다."),

    //exchange
    EXCHANGE_NOT_FOUND(NOT_FOUND, "해당 조건에 맞는 거래소를 찾을 수 없습니다."),
    DUPLICATE_EXCHANGE(BAD_REQUEST, "중복되는 거래소 명입니다."),
    EMPTY_MARKET_EXCHANGE(INTERNAL_SERVER_ERROR, "해당 거래소의 마켓이 존재하지 않습니다"),

    //ticker
    TICKER_NOT_FOUND(NOT_FOUND, "해당 조건에 해당하는 티커가 없습니다."),

    //exchangeMarket
    EXCHANGE_MARKET_NOT_FOUND(NOT_FOUND, "해당 조건에 맞는 ExchangeMarket엔터티를 찾을 수 없습니다"),

    //fetch
    CAST_FAILED(INTERNAL_SERVER_ERROR, "타입 케스트에 실패했습니다."),
    EXTERNAL_SERVICE_COMMUNICATION_FAILED(BAD_GATEWAY, "외부 서비스와의 통신에 실패했습니다");



    private final HttpStatus httpStatus;
    private final String message;
}
