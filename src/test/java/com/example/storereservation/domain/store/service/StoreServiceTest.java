package com.example.storereservation.domain.store.service;

import com.example.storereservation.domain.partner.dto.RegisterPartner;
import com.example.storereservation.domain.partner.service.PartnerService;
import com.example.storereservation.domain.partner.service.PartnerStoreService;
import com.example.storereservation.domain.partner.dto.AddStore;
import com.example.storereservation.domain.store.dto.StoreDetail;
import com.example.storereservation.global.exception.ErrorCode;
import com.example.storereservation.global.exception.MyException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class StoreServiceTest {

    @Autowired
    StoreService storeService;
    @Autowired
    PartnerService partnerService;
    @Autowired
    PartnerStoreService partnerStoreService;

    private static final String PARTNER_ID =  "tPartnerId";
    private static final String STORE_NAME =  "tStoreName";


    @BeforeEach
    void addStore(){
        RegisterPartner.Request partnerRequest = RegisterPartner.Request.builder()
                .partnerId(PARTNER_ID)
                .password("asdf")
                .passwordCheck("asdf")
                .partnerName("name")
                .phone("01011112222")
                .build();
        partnerService.register(partnerRequest);

        AddStore.Request request = AddStore.Request.builder()
                .storeName(STORE_NAME)
                .storeAddr("tStoreAddr")
                .text("test text")
                .build();
        partnerStoreService.addStore(PARTNER_ID, request);
    }

    @Test
    @DisplayName("findByStoreName_성공")
    void findByStoreName() {
        //given : beforeEach에서 store 생성 (ID : STORE_NAME)

        //when
        StoreDetail findStore = storeService.findByStoreName(STORE_NAME);

        //then
        assertThat(findStore.getStoreName()).isEqualTo(STORE_NAME);

    }
    @Test
    @DisplayName("findByStoreName_매장명이 존재하지 않음")
    void findByStoreName_STORE_NOT_FOUND() {
        //given : beforeEach에서 store 생성 (ID : STORE_NAME)

        //when
        try{
            storeService.findByStoreName(STORE_NAME + "asdf");
        }//then
        catch(MyException e){
            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.STORE_NOT_FOUND);
        }




    }
}