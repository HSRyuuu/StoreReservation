package com.example.storereservation.domain.store.mybatis;

import com.example.storereservation.domain.store.dto.StoreDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StoreMapper {

    List<StoreDto> findStoreListOrderByDistance(
            @Param("storeName") String storeName,
            @Param("lat") double lat,
            @Param("lnt") double lnt,
            @Param("start") Integer start,
            @Param("end") Integer end
            );
}
