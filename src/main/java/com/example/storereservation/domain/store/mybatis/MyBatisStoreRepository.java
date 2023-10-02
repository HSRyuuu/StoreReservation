package com.example.storereservation.domain.store.mybatis;

import com.example.storereservation.domain.store.dto.StoreDto;
import com.example.storereservation.domain.store.dto.StoreListQuery;
import com.example.storereservation.global.type.PageConst;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MyBatisStoreRepository {
    private final StoreMapper storeMapper;

    public List<StoreDto> findByStoreNameOrderByDistance(StoreListQuery input, Integer page){
        Integer size = PageConst.STORE_LIST_PAGE_SIZE;
        return
                storeMapper.findStoreListOrderByDistance(
                        input.getStoreName(),
                        input.getLat(),
                        input.getLnt(),
                        size * page, size);
    }
}
