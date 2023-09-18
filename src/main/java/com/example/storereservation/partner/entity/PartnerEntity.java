package com.example.storereservation.partner.entity;

import com.example.storereservation.web.security.MemberType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity(name = "PARTNER")
public class PartnerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String partnerId;
    private String password;

    private String partnerName;
    private String phone;

    private Long storeId;
    private String storeName;

    @Enumerated(EnumType.STRING)
    private MemberType memberType; //ROLE_PARTNER

    private LocalDateTime createAt;
    private LocalDateTime updateDt;

    public void setStore(Long storeId, String storeName){
        this.storeId = storeId;
        this.storeName = storeName;
    }
}
