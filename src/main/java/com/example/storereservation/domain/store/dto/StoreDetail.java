package com.example.storereservation.domain.store.dto;

import com.example.storereservation.domain.store.persist.StoreEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class StoreDetail {
    private String storeName;
    private String storeAddr;
    private String text;
    private String distance;
    private String rating;
    private Long count;

    public static StoreDetail fromEntity(StoreEntity store){
        return StoreDetail.builder()
                .storeName(store.getStoreName())
                .storeAddr(store.getStoreAddr())
                .text(store.getText())
                .rating(String.format("%.2f", store.getRating()))
                .count(store.getRatingCount())
                .build();
    }

    public static StoreDetail fromDto(StoreDto store){
        return StoreDetail.builder()
                .storeName(store.getStoreName())
                .storeAddr(store.getStoreAddr())
                .text(store.getText())
                .distance(String.format("%.3fkm",store.getDistance()))
                .rating(String.format("%.2f", store.getRating()))
                .count(store.getRatingCount())
                .build();
    }
}
