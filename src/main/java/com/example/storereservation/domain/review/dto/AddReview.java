package com.example.storereservation.domain.review.dto;

import com.example.storereservation.domain.reservation.persist.ReservationEntity;
import com.example.storereservation.domain.review.persist.ReviewEntity;
import com.example.storereservation.domain.user.dto.RegisterUser;
import com.example.storereservation.domain.user.dto.UserDto;
import com.example.storereservation.domain.user.persist.UserEntity;
import com.example.storereservation.global.auth.type.MemberType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


public class AddReview {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request{
        private double rating;
        private String text;

        public static ReviewEntity toEntity(Request request, ReservationEntity reservation){
            return ReviewEntity.builder()
                    .reservationId(reservation.getId())
                    .userId(reservation.getUserId())
                    .storeName(reservation.getStoreName())
                    .rating(request.getRating())
                    .text(request.getText())
                    .createdAt(LocalDateTime.now())
                    .build();
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response{
        private String userId;
        private String storeName;
        private double rating;
        private String text;
        private LocalDateTime createdAt;

        public static Response fromDto(ReviewDto review){
            return Response.builder()
                    .userId(review.getUserId())
                    .storeName(review.getStoreName())
                    .rating(review.getRating())
                    .text(review.getText())
                    .createdAt(review.getCreatedAt())
                    .build();
        }
    }
}
