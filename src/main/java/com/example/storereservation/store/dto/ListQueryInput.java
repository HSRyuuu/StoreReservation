package com.example.storereservation.store.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListQueryInput {
    private String storeName;
    private String sortType;
}
