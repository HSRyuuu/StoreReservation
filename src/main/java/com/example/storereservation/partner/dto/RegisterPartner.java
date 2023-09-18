package com.example.storereservation.partner.dto;


import com.example.storereservation.partner.entity.PartnerEntity;
import com.example.storereservation.web.security.MemberType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


public class RegisterPartner {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request{
        private String partnerId;

        private String password;
        private String passwordCheck;

        private String partnerName;
        private String phone;

        private MemberType memberType;//ROLE_MANAGER

        public static PartnerEntity toEntity(Request request){
            return PartnerEntity.builder()
                    .partnerId(request.getPartnerId())
                    .password(request.getPassword())
                    .partnerName(request.getPartnerName())
                    .phone(request.getPhone())
                    .memberType(MemberType.ROLE_PARTNER)
                    .createAt(LocalDateTime.now())
                    .build();
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response{
        private String partnerId;

        private String partnerName;
        private String phone;


        private LocalDateTime createAt;
        private MemberType memberType;

        public static Response fromDto(PartnerDto partnerDto){
            return Response.builder()
                    .partnerId(partnerDto.getPartnerId())
                    .partnerName(partnerDto.getPartnerName())
                    .phone(partnerDto.getPhone())
                    .memberType(partnerDto.getMemberType())
                    .createAt(partnerDto.getCreateAt())
                    .build();
        }
    }

}