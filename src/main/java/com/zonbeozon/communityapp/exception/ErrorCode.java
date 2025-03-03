package com.zonbeozon.communityapp.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    //common
    SERVER_ERROR(INTERNAL_SERVER_ERROR, "Internal Server Error"),
    // auth
    ILLEGAL_REGISTRATION_ID(NOT_ACCEPTABLE, "illegal registration id"),
    TOKEN_EXPIRED(UNAUTHORIZED, "토큰이 만료되었습니다."),
    INVALID_TOKEN(UNAUTHORIZED, "올바르지 않은 토큰입니다."),
    INVALID_JWT_SIGNATURE(UNAUTHORIZED, "잘못된 JWT 시그니처입니다."),

    //exchange
    EXCHANGE_NOT_FOUND(NOT_FOUND, "해당 조건에 맞는 거래소를 찾을 수 없습니다."),
    DUPLICATE_EXCHANGE(BAD_REQUEST, "중복되는 거래소 명입니다."),
    EMPTY_MARKET_EXCHANGE(INTERNAL_SERVER_ERROR, "해당 거래소의 마켓이 존재하지 않습니다"),

    //currency
    DUPLICATE_CURRENCY_SYMBOL(BAD_REQUEST, "중복되는 심볼입니다"),
    CURRENCY_NOT_FOUND(NOT_FOUND, "해당 조건에 맞는 Currency를 찾을 수 없습니다."),

    //market
    MARKET_NOT_FOUND(NOT_FOUND, "해당 조건에 맞는 마켓을 찾을 수 없습니다."),
    DUPLICATE_MARKET(BAD_REQUEST, "중복되는 마켓입니다."),
    ILLEGAL_MARKET_STATUS(BAD_REQUEST, " 존재하지 않는 마켓 상태입니다."),
    ILLEGAL_MARET_CODE(BAD_REQUEST, "지원하지 않는 마켓 코드입니다."),

    //fetch
    EXTERNAL_SERVICE_COMMUNICATION_FAILED(BAD_GATEWAY, "외부 서비스와의 통신에 실패했습니다"),
    FETCH_VALIDATION_FAILED(BAD_GATEWAY, "데이터 검증에 실패했습니다."),

    //exchange rate
    EXCHANGE_RATE_NOT_AVAILABLE(INTERNAL_SERVER_ERROR, "환율 정보를 가져올 수 없습니다(휴장일)."),
    EMPTY_EXCHANGE_RATE(INTERNAL_SERVER_ERROR, "등록된 환율 정보가 없습니다."),

    //fiat
    ILLEGAL_FIAT_TYPE(BAD_REQUEST, "존재하지 않는 통화입니다.");


    private final HttpStatus httpStatus;
    private final String message;
}
