package com.example.storereservation.domain.store.persist;

import com.example.storereservation.domain.store.dto.EditStore;
import lombok.*;
import org.springframework.util.StringUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "STORE")
public class StoreEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String partnerId;

    private String storeName;
    private String storeAddr;
    private String text;

    private double lat;
    private double lnt;

    private double rating;
    private Long ratingCount;

    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public void edit(EditStore.Request request){
        if(StringUtils.hasText(request.getStoreName())){
            this.storeName = request.getStoreName();
        }
        if(StringUtils.hasText(request.getStoreAddr())){
            this.storeAddr = request.getStoreAddr();
        }
        if(StringUtils.hasText(request.getText())){
            this.text = request.getText();
        }
        this.updateAt = LocalDateTime.now();
    }



}
