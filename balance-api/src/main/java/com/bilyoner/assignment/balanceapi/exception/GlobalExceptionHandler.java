package com.bilyoner.assignment.balanceapi.exception;

import com.bilyoner.assignment.balanceapi.model.ErrorResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@Slf4j
@AllArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BalanceApiException.class)
    public final ResponseEntity<ErrorResponse> handleBalanceApiException(BalanceApiException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setDateTime(LocalDateTime.now());
        errorResponse.setCode(ex.getErrorCode().getCode());
        errorResponse.setMessage(ex.getErrorCode().getMessage());
        log.error("BalanceApiException ", ex);
        return new ResponseEntity<>(errorResponse, ex.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse globalExceptionHandler(Exception ex) {
        ErrorResponse message = new ErrorResponse(ErrorCodeEnum.INTERNAL_SERVER_ERROR.getCode(), ErrorCodeEnum.INTERNAL_SERVER_ERROR.getMessage(),
                                                  LocalDateTime.now());

        log.error("INTERNAL_SERVER_ERROR ", ex);
        return message;
    }
}
