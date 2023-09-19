package com.example.storereservation.domain.store.persist;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Setter
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

    private double rating;
    private Long ratingCount;

    private LocalDateTime createAt;
    private LocalDateTime updateAt;



}
