package com.bilyoner.assignment.couponapi.exception;

import com.bilyoner.assignment.couponapi.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CouponApiException.class)
    public final ResponseEntity<ErrorResponse> handleBalanceApiException(CouponApiException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setDateTime(LocalDateTime.now());
        errorResponse.setCode(ex.getErrorCode().getCode());
        errorResponse.setMessage(ex.getErrorCode().getMessage());
        log.error("CouponApiException ", ex);
        return new ResponseEntity<>(errorResponse, ex.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse globalExceptionHandler(Exception ex) {
        ErrorResponse message = new ErrorResponse(ErrorCodeEnum.INTERNAL_SERVER_ERROR.getCode(), ErrorCodeEnum.INTERNAL_SERVER_ERROR.getMessage(),
                                                  LocalDateTime.now());

        log.error("INTERNAL_SERVER_ERROR", ex);
        return message;
    }
}