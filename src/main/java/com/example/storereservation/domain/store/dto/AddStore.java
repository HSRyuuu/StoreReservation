package com.example.storereservation.domain.store.dto;


import com.example.storereservation.domain.store.persist.StoreEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


public class AddStore {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request{
        private String storeName;
        private String storeAddr;
        private String text;

        public static StoreEntity toEntity(Request request, String partnerId){
            return StoreEntity.builder()
                    .partnerId(partnerId)
                    .storeName(request.getStoreName())
                    .storeAddr(request.getStoreAddr())
                    .text(request.getText())
                    .createAt(LocalDateTime.now())
                    .rating(0.0)
                    .ratingCount(0L)
                    .build();
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response{
        private String partnerId;

        private String storeName;
        private String storeAddr;
        private String text;
        private LocalDateTime createAt;

        public static Response fromDto(StoreDto storeDto){
            return Response.builder()
                    .partnerId(storeDto.getPartnerId())
                    .storeName(storeDto.getStoreName())
                    .storeAddr(storeDto.getStoreAddr())
                    .text(storeDto.getText())
                    .createAt(storeDto.getCreateAt())
                    .build();
        }
    }

}
