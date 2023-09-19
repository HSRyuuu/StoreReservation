package com.example.storereservation.domain.partner.service;

import com.example.storereservation.domain.partner.dto.EditStore;
import com.example.storereservation.domain.partner.persist.PartnerEntity;
import com.example.storereservation.domain.partner.persist.PartnerRepository;
import com.example.storereservation.domain.partner.dto.AddStore;
import com.example.storereservation.domain.store.dto.StoreDto;
import com.example.storereservation.domain.store.persist.StoreEntity;
import com.example.storereservation.domain.store.persist.StoreRepository;
import com.example.storereservation.global.exception.ErrorCode;
import com.example.storereservation.global.exception.MyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PartnerStoreService {

    private final PartnerRepository partnerRepository;
    private final StoreRepository storeRepository;

    /**
     * 매장 추가하기
     * 1. PARTNER_DOESNT_EXIST : 파트너 존재 확인
     * 2. requestValidate
     * -> PARTNER_ALREADY_HAS_STORE : 해당 파트너가 이미 매장을 등록했는지 확인
     * -> STORE_NAME_ALREADY_EXISTS : 해당 매장명이 이미 존재하는지 확인
     * 3. 해당 파트너 엔티티에 storeId, storeName 저장
     */
    public StoreDto addStore(String partnerId, AddStore.Request request){
        PartnerEntity partner = partnerRepository.findByPartnerId(partnerId)
                .orElseThrow(() -> new MyException(ErrorCode.PARTNER_NOT_FOUND));

        this.requestValidate(partnerId, request.getStoreName());

        StoreEntity savedStore = storeRepository.save(
                AddStore.Request.toEntity(request, partnerId)
        );

        partner.setStore(savedStore.getId(), savedStore.getStoreName());
        partnerRepository.save(partner);

        return StoreDto.fromEntity(savedStore);
    }

    private void requestValidate(String partnerId, String storeName){
        if(storeRepository.existsByPartnerId(partnerId)){
            throw new MyException(ErrorCode.PARTNER_ALREADY_HAS_STORE);
        }else if(storeRepository.existsByStoreName(storeName)){
            throw new MyException(ErrorCode.STORE_NAME_ALREADY_EXISTS);
        }
    }

    /**
     * 매장 정보 수정
     * @param request
     * @return
     */
    public StoreDto editStore(String partnerId, EditStore.Request request){
        if(!partnerRepository.existsByPartnerId(partnerId)){
            throw new MyException(ErrorCode.PARTNER_NOT_FOUND);
        }

        StoreEntity store = storeRepository.findByPartnerId(partnerId)
                .orElseThrow(() -> new MyException(ErrorCode.STORE_NOT_FOUND));

        if(!store.getStoreName().equals(request.getStoreName())
                && storeRepository.existsByStoreName(request.getStoreName())){
            throw new MyException(ErrorCode.STORE_NAME_ALREADY_EXISTS);
        }

        store.edit(request);

        StoreEntity updated = storeRepository.save(store);

        return StoreDto.fromEntity(updated);
    }

}
