package com.example.storereservation.domain.review.dto;

import com.example.storereservation.domain.review.persist.ReviewEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewDto {
    private Long id;
    private Long reservationId;
    private String userId;
    private String storeName;
    private double rating;
    private String text;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ReviewDto fromEntity(ReviewEntity reviewEntity){
        return ReviewDto.builder()
                .id(reviewEntity.getId())
                .reservationId(reviewEntity.getReservationId())
                .userId(reviewEntity.getUserId())
                .storeName(reviewEntity.getStoreName())
                .rating(reviewEntity.getRating())
                .text(reviewEntity.getText())
                .createdAt(reviewEntity.getCreatedAt())
                .updatedAt(reviewEntity.getUpdatedAt())
                .build();
    }
}
