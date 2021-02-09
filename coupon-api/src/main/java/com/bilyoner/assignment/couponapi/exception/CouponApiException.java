package com.bilyoner.assignment.couponapi.exception;

import lombok.Getter;

import java.util.function.Supplier;

@Getter
public class CouponApiException extends RuntimeException implements Supplier<CouponApiException> {

    private final ErrorCodeEnum errorCode;

    public CouponApiException(ErrorCodeEnum errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public CouponApiException(String message, ErrorCodeEnum errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    @Override
    public CouponApiException get() {
        return this;
    }
}
