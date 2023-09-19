package com.example.storereservation.domain.partner.service;

import com.example.storereservation.domain.partner.dto.PartnerDto;
import com.example.storereservation.domain.partner.dto.RegisterPartner;
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
class PartnerServiceTest {

    @Autowired
    PartnerService partnerService;

    @Test
    @DisplayName("파트너 register_정상")
    void register() {
        //given
        RegisterPartner.Request request = RegisterPartner.Request.builder()
                .partnerId("tPartnerId")
                .password("asdf")
                .passwordCheck("asdf")
                .partnerName("name")
                .phone("01011112222")
                .build();
        //when
        PartnerDto partner = partnerService.register(request);
        log.info("Registered partner : {}", partner.toString());
        //then
        assertThat(partner.getPartnerId()).isEqualTo(request.getPartnerId());
        assertThat(partner.getMemberType()).isEqualTo(MemberType.ROLE_PARTNER.toString());
    }

    @Test
    @DisplayName("!!!파트너 register_회원가입_비밀번호 확인 불일치")
    void register_Fail_Password() {
        //given
        RegisterPartner.Request request = RegisterPartner.Request.builder()
                .partnerId("tPartnerId")
                .password("asdf")
                .passwordCheck("asdf1231231")
                .partnerName("name")
                .phone("01011112222")
                .build();
        //when
        try {
            partnerService.register(request);
        }//then
        catch (MyException e) {
            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.PASSWORD_CHECK_INCORRECT);
        }
    }

    @Test
    @DisplayName("!!!파트너 register_이미 존재하는 파트너")
    void register_DUPLICATED_PARTNER() {
        //given
        RegisterPartner.Request request1 = RegisterPartner.Request.builder()
                .partnerId("tPartnerId")
                .password("asdf")
                .passwordCheck("asdf")
                .partnerName("name")
                .phone("01011112222")
                .build();
        partnerService.register(request1);

        RegisterPartner.Request request2 = RegisterPartner.Request.builder()
                .partnerId("tPartnerId")
                .password("asdf")
                .passwordCheck("asdf")
                .partnerName("name")
                .phone("01011112222")
                .build();
        //when
        try {
            partnerService.register(request2);
        }//then
        catch (MyException e) {
            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.DUPLICATED_ID);
        }
    }
}