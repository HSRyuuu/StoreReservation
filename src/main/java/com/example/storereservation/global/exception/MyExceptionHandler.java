package com.example.storereservation.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(MyException.class)
    protected ErrorResponse handleCustomException(MyException e){
        ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode().getStatusCode(), e.getErrorCode(), e.getErrorMessage());
        return errorResponse;
    }
}
