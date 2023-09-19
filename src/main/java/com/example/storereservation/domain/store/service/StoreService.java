package com.example.storereservation.domain.store.service;

import com.example.storereservation.global.exception.ErrorCode;
import com.example.storereservation.global.exception.MyException;
import com.example.storereservation.domain.store.type.PageConst;
import com.example.storereservation.domain.store.dto.StoreDetail;
import com.example.storereservation.domain.store.persist.StoreEntity;
import com.example.storereservation.domain.store.persist.StoreRepository;
import com.example.storereservation.domain.store.type.SortType;
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
