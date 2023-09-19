package com.example.storereservation.domain.user.service;

import com.example.storereservation.domain.user.dto.RegisterUser;
import com.example.storereservation.domain.user.dto.UserDto;
import com.example.storereservation.global.auth.type.MemberType;
import com.example.storereservation.global.exception.ErrorCode;
import com.example.storereservation.global.exception.MyException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    @DisplayName("유저 register_정상")
    void register() {
        //given
        RegisterUser.Request request = RegisterUser.Request.builder()
                .userId("tUserId")
                .password("asdf")
                .passwordCheck("asdf")
                .name("tName")
                .phone("01011112222")
                .build();
        //when
        UserDto user = userService.register(request);

        //then
        assertThat(user.getUserId()).isEqualTo(request.getUserId());
        assertThat(user.getMemberType()).isEqualTo(MemberType.ROLE_USER.toString());
    }

    @Test
    @DisplayName("!!!유저 register_비밀번호 확인 불일치")
    void register_FAIL_PASSWORD_CHECK_INCORRECT() {
        //given
        RegisterUser.Request request = RegisterUser.Request.builder()
                .userId("tUserId")
                .password("asdf")
                .passwordCheck("asdf123123")
                .name("tName")
                .phone("01011112222")
                .build();

        //when
        try {
            userService.register(request);
        }//then
        catch (MyException e) {
            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.PASSWORD_CHECK_INCORRECT);
        }
    }
    @Test
    @DisplayName("!!!유저 register_중복 유저")
    void register_FAIL_DUPLICATED_USER() {
        //given
        RegisterUser.Request request1 = RegisterUser.Request.builder()
                .userId("tUserId")
                .password("asdf")
                .passwordCheck("asdf")
                .name("tName")
                .phone("01011112222")
                .build();
        RegisterUser.Request request2 = RegisterUser.Request.builder()
                .userId("tUserId")
                .password("asdf")
                .passwordCheck("asdf")
                .name("tName")
                .phone("01011112222")
                .build();
        userService.register(request1);
        //when
        try {
            userService.register(request2);
        }//then
        catch (MyException e) {
            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.DUPLICATED_ID);
        }


    }
}