package com.bilyoner.assignment.couponapi.exception;

public class NotFoundException extends CouponApiException {

    public NotFoundException(String message) {
        super(message + "can not found", ErrorCodeEnum.CONTENT_NOT_FOUND_ERROR);
    }

}
