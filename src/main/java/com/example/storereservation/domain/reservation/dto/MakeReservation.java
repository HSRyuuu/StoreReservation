package com.example.storereservation.domain.reservation.dto;

import com.example.storereservation.domain.reservation.type.ReservationStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


public class MakeReservation {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request{
        private String userId;
        private String storeName;
        private Integer people;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate date;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        private LocalTime time;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response{
        private String userId;
        private String phone;
        private String storeName;

        private Integer people;
        private ReservationStatus status;

        private LocalDateTime time;

        public static Response fromDto(ReservationDto reservationDto){
            return Response.builder()
                    .userId(reservationDto.getUserId())
                    .phone(reservationDto.getPhone())
                    .storeName(reservationDto.getStoreName())
                    .people(reservationDto.getPeople())
                    .status(reservationDto.getStatus())
                    .time(reservationDto.getTime())
                    .build();
        }

    }
}
