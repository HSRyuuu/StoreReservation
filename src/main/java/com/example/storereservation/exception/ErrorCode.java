package com.example.storereservation.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    //MEMBER 관련
    PASSWORD_CHECK_ERROR(HttpStatus.BAD_REQUEST.value(), "비밀번호가 일치하지 않습니다."),
    DUPLICATED_ID(HttpStatus.CONFLICT.value(), "이미 존재하는 아이디 입니다."),

    //PARTNER 관련
    PARTNER_DOESNT_EXIST(HttpStatus.BAD_REQUEST.value(), "파트너가 존재하지 않습니다."),

    //STORE 관련
    PARTNER_ALREADY_HAS_STORE(HttpStatus.CONFLICT.value(), "해당 파트너의 매장이 이미 존재합니다."),
    STORE_NAME_ALREADY_EXISTS(HttpStatus.CONFLICT.value(), "해당 매장 명이 이미 존재합니다."),
    STORE_NOT_FOUND(HttpStatus.CONFLICT.value(), "해당 매장이 존재하지 않습니다."),

    //BASIC
    NOT_FOUND_ERROR(HttpStatus.NOT_FOUND.value(), "404 NOT FOUND"),
    BAD_REQUEST_ERROR(HttpStatus.BAD_REQUEST.value(), "404 NOT FOUND"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "내부 서버 오류가 발생 했습니다.");

    private final int statusCode;
    private final String description;

}
