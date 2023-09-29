package com.example.storereservation.testsetting;

import com.example.storereservation.domain.partner.persist.PartnerEntity;
import com.example.storereservation.domain.partner.persist.PartnerRepository;
import com.example.storereservation.domain.store.persist.StoreEntity;
import com.example.storereservation.domain.store.persist.StoreRepository;
import com.example.storereservation.domain.user.persist.UserEntity;
import com.example.storereservation.domain.user.persist.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

/**
 * 테스트용 클래스(유저, 파트너, 매장 엔티티 생성)
 * TEST_USER, TEST_PARTNER, TEST_STORE이 존재하지 않는 경우생성
 */
@SpringBootTest
@Slf4j
public class CreateEntitiesForTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    StoreRepository storeRepository;
    @Autowired
    PartnerRepository partnerRepository;

    @Test
    public void addEntitiesForTest(){
        UserEntity user = UserEntity.builder()
                .userId("TEST_USER")
                .password("asdf")
                .name("현식")
                .phone("01053092890")
                .memberType("ROLE_USER")
                .createAt(LocalDateTime.now())
                .build();
        if(!userRepository.existsByUserId(user.getUserId())){
            userRepository.save(user);
            log.info("유저 생성");
        }else{
            log.info("이미 TEST_USER 존재");
        }

        StoreEntity store = StoreEntity.builder()
                .partnerId("TEST_PARTNER")
                .storeName("TEST_STORE")
                .storeAddr("강동구")
                .text("스토어 설명")
                .createAt(LocalDateTime.now())
                .build();
        if(!storeRepository.existsByStoreName(store.getStoreName())){
            storeRepository.save(store);
            log.info("store 생성");
        }else{
            log.info("이미 TEST_STORE 존재");
        }



        PartnerEntity partner = PartnerEntity.builder()
                .partnerId("TEST_PARTNER")
                .password("asdf")
                .partnerName("파트너")
                .phone("01053092890")
                .storeId(store.getId())
                .storeName("TEST_STORE")
                .memberType("ROLE_PARTNER")
                .createAt(LocalDateTime.now())
                .build();
        if(!partnerRepository.existsByPartnerId(partner.getPartnerId())){
            partnerRepository.save(partner);
            log.info("파트너 생성");
        }else{
            log.info("이미 TEST_PARTNER 존재");
        }

    }
}
