package com.example.storereservation.domain.partner.dto;

import com.example.storereservation.domain.partner.persist.PartnerEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PartnerDto {
    private String partnerId;
    private String password;

    private String partnerName;
    private String phone;

    private Long storeId;
    private String storeName;

    private String memberType; //ROLE_MANAGER

    private LocalDateTime createAt;
    private LocalDateTime updateDt;

    public static PartnerDto fromEntity(PartnerEntity partnerEntity){
        return PartnerDto.builder()
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
