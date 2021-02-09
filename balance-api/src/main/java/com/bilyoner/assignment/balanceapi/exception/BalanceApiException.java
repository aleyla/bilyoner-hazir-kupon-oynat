package com.bilyoner.assignment.balanceapi.exception;

import lombok.Getter;

import java.util.function.Supplier;

@Getter
public class BalanceApiException extends RuntimeException implements Supplier<BalanceApiException> {

    private final ErrorCodeEnum errorCode;

    public BalanceApiException(ErrorCodeEnum errorCode) {
        super();
        this.errorCode = errorCode;
    }

    @Override
    public BalanceApiException get() {
        return this;
    }
}
