package com.example.storereservation.domain.partner.service;

import com.example.storereservation.domain.partner.dto.EditStore;
import com.example.storereservation.domain.partner.dto.RegisterPartner;
import com.example.storereservation.domain.partner.persist.PartnerEntity;
import com.example.storereservation.domain.partner.persist.PartnerRepository;
import com.example.storereservation.domain.partner.dto.AddStore;
import com.example.storereservation.domain.store.dto.StoreDto;
import com.example.storereservation.domain.store.persist.StoreEntity;
import com.example.storereservation.domain.store.persist.StoreRepository;
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
    @Autowired
    StoreRepository storeRepository;

    private static final String PARTNER_ID_1 =  "tPartnerId1";
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
    @DisplayName("!!!addStore_해당 파트너가 존재하지 않음")
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
    @DisplayName("!!!addStore_파트너가 이미 매장을 등록함")
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
    @DisplayName("!!!addStore_매장명이 이미 있음")
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

    @Test
    @DisplayName("editStore_정상_모든 파라미터")
    void editStore_All_Parameters() {
        //given
        AddStore.Request addRequest = AddStore.Request.builder()
                .storeName("tStoreName")
                .storeAddr("tStoreAddr")
                .text("test text")
                .build();
        StoreDto storeAdd = partnerStoreService.addStore(PARTNER_ID_1, addRequest);

        //when
        EditStore.Request editRequest = EditStore.Request.builder()
                .storeName("edit")
                .storeAddr("edit")
                .text("edit")
                .build();
        partnerStoreService.editStore(PARTNER_ID_1, editRequest);

        StoreEntity storeEdit = storeRepository.findByPartnerId(PARTNER_ID_1).get();

        //then
        assertThat(storeEdit.getStoreName()).isEqualTo("edit");
        assertThat(storeEdit.getStoreAddr()).isEqualTo("edit");
        assertThat(storeEdit.getText()).isEqualTo("edit");
    }

    @Test
    @DisplayName("editStore_정상_일부 파라미터")
    void editStore_Not_All_Parameters() {
        //given
        AddStore.Request addRequest = AddStore.Request.builder()
                .storeName("tStoreName")
                .storeAddr("tStoreAddr")
                .text("test text")
                .build();
        StoreDto storeAdd = partnerStoreService.addStore(PARTNER_ID_1, addRequest);

        //when
        EditStore.Request editRequest = EditStore.Request.builder()
                .text("edit")
                .build();
        partnerStoreService.editStore(PARTNER_ID_1, editRequest);

        StoreEntity storeEdit = storeRepository.findByPartnerId(PARTNER_ID_1).get();

        //then
        assertThat(storeEdit.getStoreName()).isEqualTo("tStoreName");
        assertThat(storeEdit.getStoreAddr()).isEqualTo("tStoreAddr");
        assertThat(storeEdit.getText()).isEqualTo("edit");
    }

    @Test
    @DisplayName("!!!editStore_파트너가 존재하지 않음")
    void editStore_PARTNER_NOT_FOUND() {
        //given
        AddStore.Request addRequest = AddStore.Request.builder()
                .storeName("tStoreName")
                .storeAddr("tStoreAddr")
                .text("test text")
                .build();
        StoreDto storeAdd = partnerStoreService.addStore(PARTNER_ID_1, addRequest);

        //when
        EditStore.Request editRequest = EditStore.Request.builder()
                .storeName("edit")
                .storeAddr("edit")
                .text("edit")
                .build();

        try{
            partnerStoreService.editStore("asdfsadf", editRequest);
        }//then
        catch(MyException e){
            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.PARTNER_NOT_FOUND);
        }
    }

    @Test
    @DisplayName("!!!editStore_변경하려는 상점 명이 이미 존재함")
    void editStore_STORE_NAME_ALREADY_EXISTS() {
        //given
        AddStore.Request addRequest1 = AddStore.Request.builder()
                .storeName("tStoreName1")
                .storeAddr("tStoreAddr")
                .text("test text")
                .build();
        StoreDto storeAdd = partnerStoreService.addStore(PARTNER_ID_1, addRequest1);

        AddStore.Request addRequest2 = AddStore.Request.builder()
                .storeName("tStoreName2")
                .storeAddr("tStoreAddr")
                .text("test text")
                .build();
        partnerStoreService.addStore(PARTNER_ID_2, addRequest2);

        //when
        EditStore.Request editRequest = EditStore.Request.builder()
                .storeName("tStoreName2")
                .storeAddr("edit")
                .text("edit")
                .build();

        try{
            partnerStoreService.editStore(PARTNER_ID_1, editRequest);
        }//then
        catch(MyException e){
            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.STORE_NAME_ALREADY_EXISTS);
        }
    }

    @Test
    @DisplayName("!!!editStore_해당 파트너의 매장이 존재하지 않음")
    void editStore_STORE_NOT_FOUND() {
        //given
        AddStore.Request addRequest = AddStore.Request.builder()
                .storeName("tStoreName")
                .storeAddr("tStoreAddr")
                .text("test text")
                .build();
        partnerStoreService.addStore(PARTNER_ID_1, addRequest);

        //when
        EditStore.Request editRequest = EditStore.Request.builder()
                .storeName("edit")
                .storeAddr("edit")
                .text("edit")
                .build();

        try{
            partnerStoreService.editStore(PARTNER_ID_2, editRequest);
        }//then
        catch(MyException e){
            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.STORE_NOT_FOUND);
        }
    }



}