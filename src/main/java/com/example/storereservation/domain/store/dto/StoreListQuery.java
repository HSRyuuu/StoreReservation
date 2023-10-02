package com.example.storereservation.domain.store.dto;

import com.example.storereservation.global.type.StoreSortType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreListQuery {
    private String storeName;
    private StoreSortType sortType;

    private double lat;
    private double lnt;
}
