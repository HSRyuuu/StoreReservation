package com.example.storereservation.domain.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserArrivedComplete {
    private Long reservationId;
    private String storeName;
    private String userId;
    private LocalDateTime arrivedTime;
    private LocalDateTime reservationTime;

    public UserArrivedComplete(ReservationDto reservation){
        this.reservationId = reservation.getId();
        this.storeName = reservation.getStoreName();
        this.userId = reservation.getUserId();
        this.arrivedTime = LocalDateTime.now();
        this.reservationTime = reservation.getTime();
    }
}
