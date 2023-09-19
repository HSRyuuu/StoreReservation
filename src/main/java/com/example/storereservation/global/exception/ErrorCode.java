package com.example.storereservation.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    //MEMBER 관련
    PASSWORD_CHECK_INCORRECT(HttpStatus.BAD_REQUEST.value(), "비밀번호 확인이 일치하지 않습니다."),
    DUPLICATED_ID(HttpStatus.CONFLICT.value(), "이미 존재하는 아이디 입니다."),
    PASSWORD_INCORRECT(HttpStatus.CONFLICT.value(), "비밀번호가 일치하지 않습니다."),

    //PARTNER 관련
    PARTNER_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "해당 파트너가 존재하지 않습니다."),


    //USER 관련
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "해당 유저가 존재하지 않습니다."),

    //STORE 관련
    PARTNER_ALREADY_HAS_STORE(HttpStatus.CONFLICT.value(), "해당 파트너의 매장이 이미 존재합니다."),
    STORE_NAME_ALREADY_EXISTS(HttpStatus.CONFLICT.value(), "해당 매장 명이 이미 존재합니다."),
    STORE_NOT_FOUND(HttpStatus.CONFLICT.value(), "해당 매장이 존재하지 않습니다."),
    STORE_PARTNER_NOT_MATCH(HttpStatus.FORBIDDEN.value(), "해당 파트너의 매장이 아닙니다."),

    //RESERVATION 관련
    INPUT_USER_ID_NOT_MATCH(HttpStatus.FORBIDDEN.value(), "입력받은 유저 아이디가 맞지 않습니다."),


    //Security
    TOKEN_TIME_OUT(HttpStatus.CONFLICT.value(), "토큰이 만료되었습니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN.value(), "해당 사용자는 접근할 수 없는 페이지 입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value(), "로그인이 되지 않았습니다."),

    //BASIC
    NOT_FOUND_ERROR(HttpStatus.NOT_FOUND.value(), "404 NOT FOUND"),
    BAD_REQUEST_ERROR(HttpStatus.BAD_REQUEST.value(), "404 NOT FOUND"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "내부 서버 오류가 발생 했습니다.");

    private final int statusCode;
    private final String description;

}
