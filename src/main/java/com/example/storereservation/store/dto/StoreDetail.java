package com.example.storereservation.store.dto;

import com.example.storereservation.store.entity.StoreEntity;
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
    private String rating;

    public static StoreDetail fromEntity(StoreEntity store){
        return StoreDetail.builder()
                .storeName(store.getStoreName())
                .storeAddr(store.getStoreAddr())
                .text(store.getText())
                .rating(String.format("%.1f", store.getRating()))
                .build();
    }
}
