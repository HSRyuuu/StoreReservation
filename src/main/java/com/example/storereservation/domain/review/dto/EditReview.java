package com.example.storereservation.domain.review.dto;

import com.example.storereservation.domain.reservation.persist.ReservationEntity;
import com.example.storereservation.domain.review.persist.ReviewEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class EditReview {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request{
        private double rating;
        private String text;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response{
        private String storeName;
        private String userId;
        private double rating;
        private String text;

        public static Response fromDto(ReviewDto review){
            return Response.builder()
                    .storeName(review.getStoreName())
                    .userId(review.getUserId())
                    .rating(review.getRating())
                    .text(review.getText())
                    .build();
        }
    }
}
