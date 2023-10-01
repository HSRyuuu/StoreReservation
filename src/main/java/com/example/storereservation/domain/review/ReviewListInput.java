package com.example.storereservation.domain.review;

import com.example.storereservation.global.type.ReviewSortType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewListInput {
    String storeName;
    ReviewSortType sortType;
}
