package com.example.storereservation.domain.partner.service;

import com.example.storereservation.domain.partner.dto.RegisterPartner;
import com.example.storereservation.domain.partner.persist.PartnerEntity;
import com.example.storereservation.domain.partner.persist.PartnerRepository;
import com.example.storereservation.domain.store.dto.AddStore;
import com.example.storereservation.domain.store.dto.StoreDto;
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
class PartnerStoreServiceTest {

    @Autowired
    PartnerStoreService partnerStoreService;
    @Autowired
    PartnerService partnerService;
    @Autowired
    PartnerRepository partnerRepository;

    private static final String PARTNER_ID_1 =  "tPartnerId";
    private static final String PARTNER_ID_2 =  "tPartnerId2";

    @BeforeEach
    void addPartner(){
        RegisterPartner.Request partnerRequest = RegisterPartner.Request.builder()
                .partnerId(PARTNER_ID_1)
                .password("asdf")
                .passwordCheck("asdf")
                .partnerName("name")
                .phone("01011112222")
                .build();
        partnerService.register(partnerRequest);

        RegisterPartner.Request partnerRequest2 = RegisterPartner.Request.builder()
                .partnerId(PARTNER_ID_2)
                .password("asdf")
                .passwordCheck("asdf")
                .partnerName("name")
                .phone("01011112222")
                .build();
        partnerService.register(partnerRequest2);
    }

    @Test
    @DisplayName("addStore_정상")
    void addStore() {
        //given
        PartnerEntity partner = partnerRepository.findByPartnerId(PARTNER_ID_1).get();
        AddStore.Request request = AddStore.Request.builder()
                .storeName("tStoreName")
                .storeAddr("tStoreAddr")
                .text("test text")
                .build();
        //when
        StoreDto store = partnerStoreService.addStore(partner.getPartnerId(), request);

        //then
        assertThat(store.getPartnerId()).isEqualTo(partner.getPartnerId());
        assertThat(partner.getStoreId()).isEqualTo(store.getId());
        assertThat(partner.getStoreName()).isEqualTo(store.getStoreName());
    }

    @Test
    @DisplayName("addStore_PARTNER_NOT_FOUND")
    void addStore_PARTNER_NOT_FOUND() {
        //given
        AddStore.Request request = AddStore.Request.builder()
                .storeName("tStoreName")
                .storeAddr("tStoreAddr")
                .text("test text")
                .build();
        //when
        try{
            partnerStoreService.addStore(PARTNER_ID_2, request);
        }catch(MyException e){
            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.PARTNER_NOT_FOUND);
        }
    }

    @Test
    @DisplayName("addStorePARTNER_ALREADY_HAS_STORE")
    void addStore_PARTNER_ALREADY_HAS_STORE() {
        //given
        AddStore.Request request = AddStore.Request.builder()
                .storeName("tStoreName")
                .storeAddr("tStoreAddr")
                .text("test text")
                .build();
        partnerStoreService.addStore(PARTNER_ID_1, request);
        //when
        try{
            partnerStoreService.addStore(PARTNER_ID_1, request);
        }catch(MyException e){
            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.PARTNER_ALREADY_HAS_STORE);
        }
    }

    @Test
    @DisplayName("addStore_STORE_NAME_ALREADY_EXISTS")
    void addStore_STORE_NAME_ALREADY_EXISTS() {
        //given
        AddStore.Request request = AddStore.Request.builder()
                .storeName("tStoreName")
                .storeAddr("tStoreAddr")
                .text("test text")
                .build();
        partnerStoreService.addStore(PARTNER_ID_1, request);
        //when
        try{
            partnerStoreService.addStore(PARTNER_ID_2, request);
        }catch(MyException e){
            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.STORE_NAME_ALREADY_EXISTS);
        }
    }
}