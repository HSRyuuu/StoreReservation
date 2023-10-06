package com.example.storereservation.domain.store.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


public class EditStore {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request{
        private String storeName;
        private String storeAddr;
        private String text;

        private double lat;
        private double lnt;

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

        private double lat;
        private double lnt;

        private LocalDateTime createAt;
        private LocalDateTime updateAt;

        public static Response fromDto(StoreDto storeDto){
            return Response.builder()
                    .partnerId(storeDto.getPartnerId())
                    .storeName(storeDto.getStoreName())
                    .storeAddr(storeDto.getStoreAddr())
                    .text(storeDto.getText())
                    .lat(storeDto.getLat())
                    .lnt(storeDto.getLnt())
                    .createAt(storeDto.getCreateAt())
                    .updateAt(storeDto.getUpdateAt())
                    .build();
        }
    }

}
