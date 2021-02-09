package com.bilyoner.assignment.couponapi.exception;

public class ValidationException extends CouponApiException {

    public ValidationException(String message) {
        super(message, ErrorCodeEnum.FIELD_VALIDATION_ERROR);
    }
}
