package com.example.storereservation.global.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponse {

    String description;
    String errorMessage;

    public ExceptionResponse(String description,Exception e){
        this.description = description;
        this.errorMessage = e.getMessage();
    }
}
