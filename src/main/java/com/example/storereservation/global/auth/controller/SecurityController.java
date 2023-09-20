package com.example.storereservation.global.auth.controller;

import com.example.storereservation.global.exception.ErrorCode;
import com.example.storereservation.global.exception.MyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class SecurityController {

    @GetMapping("/exception/auth-denied")
    public void accessDenied() {
        throw new MyException(ErrorCode.ACCESS_DENIED);
    }

    @GetMapping("/exception/unauthorized")
    public void unauthorized() {
        throw new MyException(ErrorCode.LOGIN_REQUIRED);
    }
}