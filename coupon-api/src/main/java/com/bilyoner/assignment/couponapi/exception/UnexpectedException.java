package com.bilyoner.assignment.couponapi.exception;

public class UnexpectedException extends CouponApiException {

    public UnexpectedException(String message) {
        super(message, ErrorCodeEnum.INTERNAL_SERVER_ERROR);
    }
}
