package com.example.storereservation.global.exception;

import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(MyException.class)
    protected ErrorResponse handleMyException(MyException e){
        ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode());
        return errorResponse;
    }


}
