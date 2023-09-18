package com.example.storereservation.partner.dto;

import com.example.storereservation.partner.entity.PartnerEntity;
import com.example.storereservation.web.security.MemberType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PartnerDto {

    private Long id;

    private String partnerId;
    private String password;

    private String partnerName;
    private String phone;

    private Long storeId;
    private String storeName;

    private MemberType memberType; //ROLE_MANAGER

    private LocalDateTime createAt;
    private LocalDateTime updateDt;

    public static PartnerDto fromEntity(PartnerEntity partnerEntity){
        return PartnerDto.builder()
                .id(partnerEntity.getId())
                .partnerId(partnerEntity.getPartnerId())
                .password(partnerEntity.getPassword())
                .partnerName(partnerEntity.getPartnerName())
                .phone(partnerEntity.getPhone())
                .memberType(partnerEntity.getMemberType())
                .createAt(partnerEntity.getCreateAt())
                .updateDt(partnerEntity.getUpdateDt())
                .build();
    }
}