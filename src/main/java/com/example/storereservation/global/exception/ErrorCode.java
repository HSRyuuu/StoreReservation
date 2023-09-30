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
    STORE_NO_SEARCH_RESULT(HttpStatus.CONFLICT.value(), "검색 결과가 없습니다."),
    STORE_PARTNER_NOT_MATCH(HttpStatus.FORBIDDEN.value(), "해당 파트너의 매장이 아닙니다."),

    //RESERVATION 관련
    RESERVATION_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "예약 정보를 찾을 수 없습니다."),
    RESERVATION_IS_ZERO(HttpStatus.BAD_REQUEST.value(), "예약 내역이 없습니다."),
    RESERVATION_STATUS_CODE_REQUIRED(HttpStatus.BAD_REQUEST.value(), "예약 상태 코드가 필요합니다." +
            " (REQUESTING, REFUSED, CONFIRM, ARRIVED, USE_COMPLETE, NO_SHOW)"),
    RESERVATION_STATUS_CODE_ILLEGAL_ARGUMENT(HttpStatus.BAD_REQUEST.value(), "해당 상태코드가 존재하지 않습니다." +
            " (REQUESTING, REFUSED, CONFIRM, ARRIVED, USE_COMPLETE, NO_SHOW)"),
    RESERVATION_UPDATE_AUTH_FAIL(HttpStatus.BAD_REQUEST.value(), "해당 파트너는 해당 예약에 대한 변경 권한이 없습니다."),

    //예약 확인 status 문제 발생 시
    RESERVATION_PHONE_NUMBER_INCORRECT(HttpStatus.BAD_REQUEST.value(), "전화번호 정보가 일치하지 않습니다. 전화번호 뒷 4자리를 다시 입력해주세요."),
    RESERVATION_STATUS_CHECK_ERROR(HttpStatus.BAD_REQUEST.value(), "예약 상태 코드에 문제가 있습니다. 가게에 문의하세요."),
    RESERVATION_TIME_CHECK_ERROR(HttpStatus.BAD_REQUEST.value(), "예약 시간에 문제가 있습니다. 가게에 문의하세요."),

    //REVIEW 관련
    REVIEW_NOT_AVAILABLE(HttpStatus.CONFLICT.value(), "해당 예약은 리뷰를 쓸 수 있는 상태가 아닙니다."),
    REVIEW_ALREADY_EXIST(HttpStatus.CONFLICT.value(), "해당 예약에 대한 리뷰가 이미 존재합니다."),
    REVIEW_RATING_RANGE_ERROR(HttpStatus.BAD_REQUEST.value(), "리뷰 숫자 범위에 문제가 있습니다.(Required : 0 ~ 5)"),
    REVIEW_TEXT_TOO_LONG(HttpStatus.BAD_REQUEST.value(), "텍스트 길이가 너무 깁니다.(200자 이내)"),
    REVIEW_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "리뷰가 존재하지 않습니다."),


    //Security
    TOKEN_TIME_OUT(HttpStatus.CONFLICT.value(), "토큰이 만료되었습니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN.value(), "접근 권한이 없습니다."),
    LOGIN_REQUIRED(HttpStatus.UNAUTHORIZED.value(), "로그인이 되지 않았습니다."),
    JWT_TOKEN_WRONG_TYPE(HttpStatus.UNAUTHORIZED.value(), "JWT 토큰 형식에 문제가 있습니다."),

    //BASIC

    NO_AUTHORITY_ERROR(HttpStatus.FORBIDDEN.value(), "권한이 없습니다."),
    NOT_FOUND_ERROR(HttpStatus.NOT_FOUND.value(), "404 NOT FOUND"),
    BAD_REQUEST_ERROR(HttpStatus.BAD_REQUEST.value(), "404 NOT FOUND"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "내부 서버 오류가 발생 했습니다.");

    private final int statusCode;
    private final String description;

}
