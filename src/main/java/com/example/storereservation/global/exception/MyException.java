package com.example.storereservation.global.exception;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class MyException extends RuntimeException{
    private ErrorCode errorCode;
    private String errorMessage;

    public MyException(ErrorCode errorCode){
        super(errorCode.getDescription());
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }
}
