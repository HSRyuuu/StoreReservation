package com.example.storereservation.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDetail {
    private String userId;
    private String rating;
    private String text;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ReviewDetail fromDto(ReviewDto reviewDto){
        return ReviewDetail.builder()
                .userId(reviewDto.getUserId())
                .rating(String.format("%.1f", reviewDto.getRating()))
                .text(reviewDto.getText())
                .createdAt(reviewDto.getCreatedAt())
                .updatedAt(reviewDto.getUpdatedAt())
                .build();
    }

}
