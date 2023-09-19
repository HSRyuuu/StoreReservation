package com.example.storereservation.global.auth.service;

import com.example.storereservation.domain.partner.dto.RegisterPartner;
import com.example.storereservation.domain.partner.persist.PartnerEntity;
import com.example.storereservation.domain.partner.service.PartnerService;
import com.example.storereservation.domain.user.dto.RegisterUser;
import com.example.storereservation.domain.user.persist.UserEntity;
import com.example.storereservation.domain.user.service.UserService;
import com.example.storereservation.global.auth.dto.LoginInput;
import com.example.storereservation.global.exception.ErrorCode;
import com.example.storereservation.global.exception.MyException;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class AuthServiceTest {

    @Autowired
    AuthService authService;
    @Autowired
    UserService userService;
    @Autowired
    PartnerService partnerService;

    private static final String USER_ID = "testID";
    private static final String USER_PW = "testPW";

    private static final String PARTNER_ID = "testID";
    private static final String PARTNER_PW = "testPW";

    @BeforeEach
    void registerUserAndPartner(){
        userService.register(RegisterUser.Request.builder()
                .userId(USER_ID)
                .password(USER_PW)
                .passwordCheck(USER_PW)
                .name("tName")
                .phone("01011112222")
                .build());
        partnerService.register(RegisterPartner.Request.builder()
                .partnerId(PARTNER_ID)
                .password(PARTNER_PW)
                .passwordCheck(PARTNER_PW)
                .partnerName("name")
                .phone("01011112222")
                .build());
    }
    @Test
    @DisplayName("유저 auth_정상")
    void authenticateUser() {
        //given
        LoginInput loginInput = new LoginInput(USER_ID, USER_PW);

        //when
        UserEntity user = authService.authenticateUser(loginInput);

        //then
        assertThat(user.getUserId()).isEqualTo(USER_ID);
    }

    @Test
    @DisplayName("!!!유저 auth_아이디 없음")
    void authenticateUser_USER_NOT_FOUND() {
        //given
        LoginInput loginInput = new LoginInput("asdfasdf", USER_PW);

        //when
        try{
            authService.authenticateUser(loginInput);
        }//then
        catch(MyException e){
            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);
        }
    }

    @Test
    @DisplayName("!!!유저 auth_비밀번호 틀림")
    void authenticateUser_PASSWORD_INCORRECT() {
        //given
        LoginInput loginInput = new LoginInput(USER_ID, "asdfasdf");

        //when
        try{
            authService.authenticateUser(loginInput);
        }//then
        catch(MyException e){
            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.PASSWORD_INCORRECT);
        }
    }

    @Test
    @DisplayName("파트너 auth_정상")
    void authenticatePartner() {
        //given
        LoginInput loginInput = new LoginInput(PARTNER_ID, PARTNER_PW);

        //when
        PartnerEntity partner = authService.authenticatePartner(loginInput);

        //then
        assertThat(partner.getPartnerId()).isEqualTo(USER_ID);
    }

    @Test
    @DisplayName("!!!파트너 auth_파트너 없음")
    void authenticatePartner_PARTNER_NOT_FOUND() {
        //given
        LoginInput loginInput = new LoginInput("asdfasdf", PARTNER_PW);

        //when
        try{
            authService.authenticatePartner(loginInput);
        }//then
        catch(MyException e){
            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.PARTNER_NOT_FOUND);
        }
    }

    @Test
    @DisplayName("!!!파트너 auth_비밀번호 틀림")
    void authenticatePartner_PASSWORD_INCORRECT() {
        //given
        LoginInput loginInput = new LoginInput(PARTNER_ID, "asdfasdfaasdf");

        //when
        try{
            authService.authenticatePartner(loginInput);
        }//then
        catch(MyException e){
            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.PASSWORD_INCORRECT);
        }
    }
}