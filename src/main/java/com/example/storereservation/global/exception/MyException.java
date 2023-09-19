package com.example.storereservation.global.exception;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyException extends RuntimeException{
    private ErrorCode errorCode;
    private String errorMessage;

    public MyException(ErrorCode errorCode){
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }
}
