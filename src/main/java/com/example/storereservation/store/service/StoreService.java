package com.example.storereservation.store.service;

import com.example.storereservation.exception.ErrorCode;
import com.example.storereservation.exception.MyException;
import com.example.storereservation.partner.entity.PartnerEntity;
import com.example.storereservation.partner.repository.PartnerRepository;
import com.example.storereservation.store.constant.PageConst;
import com.example.storereservation.store.dto.AddStore;
import com.example.storereservation.store.dto.StoreDetail;
import com.example.storereservation.store.dto.StoreDto;
import com.example.storereservation.store.entity.StoreEntity;
import com.example.storereservation.store.repository.StoreRepository;
import com.example.storereservation.store.type.SortType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class StoreService {

    private final StoreRepository storeRepository;
    private final PartnerRepository partnerRepository;

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
                .orElseThrow(() -> new MyException(ErrorCode.PARTNER_DOESNT_EXIST));

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
            //테스트를 위해 여러개의 상점을 만들 수 있도록 설정
            //throw new MyException(ErrorCode.PARTNER_ALREADY_HAS_STORE);
        }else if(storeRepository.existsByStoreName(storeName)){
            throw new MyException(ErrorCode.STORE_NAME_ALREADY_EXISTS);
        }
    }

    /**
     * 상점 명으로 상점 정보 찾기
     * - STORE_NOT_FOUND : 해당 상점명을 가진 상점이 없을 때
     */
    public StoreDetail findByStoreName(String name){
        StoreEntity findStore = storeRepository.findByStoreName(name)
                .orElseThrow(() -> new MyException(ErrorCode.STORE_NOT_FOUND));
        return StoreDetail.fromEntity(findStore);
    }

    /**
     *  상점 명으로 상점 리스트 찾기
     *  SortType : ALL / ALPHABET / RATING / REVIEW_COUNTS / DISTANCE
     *  page : constant로 저장
     */
    public Page<StoreDetail> getStoreListByStoreName(String storeName, SortType sortType, Integer page){
        PageRequest pageRequest = getPageRequestBySortTypeAndPage(sortType, page);
        Page<StoreEntity> findStores = storeRepository.findByStoreNameContaining(storeName, pageRequest);

        if(findStores.getSize() == 0){
            throw new MyException(ErrorCode.STORE_NOT_FOUND);
        }

        return findStores.map(store -> StoreDetail.fromEntity(store));
    }

    private PageRequest getPageRequestBySortTypeAndPage(SortType sortType, Integer page){
        PageRequest pageRequest = PageRequest.of(page, PageConst.STORE_LIST_SIZE);
        if(sortType == SortType.ALPHABET){
            return PageRequest.of(page, PageConst.STORE_LIST_SIZE, Sort.by("storeName"));
        }else if(sortType == SortType.RATING){
            return PageRequest.of(page, PageConst.STORE_LIST_SIZE, Sort.by("rating").descending());
        }else if(sortType == SortType.RATING_COUNT){
            return PageRequest.of(page, PageConst.STORE_LIST_SIZE, Sort.by("ratingCount").descending());
        } else if(sortType == SortType.DISTANCE){
            //TODO 거리 ???
        }
        return pageRequest;
    }
}
