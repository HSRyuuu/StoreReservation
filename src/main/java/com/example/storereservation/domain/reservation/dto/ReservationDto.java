package com.example.storereservation.domain.reservation.dto;

import com.example.storereservation.domain.reservation.persist.ReservationEntity;
import com.example.storereservation.domain.reservation.type.ReservationStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
public class ReservationDto {
    private Long id;

    private String userId;
    private String phone;

    private String partnerId;
    private String storeName;

    private Integer people;

    private ReservationStatus status;

    private LocalDateTime time;

    public static ReservationDto fromEntity(ReservationEntity reservationEntity){
        return ReservationDto.builder()
                .id(reservationEntity.getId())
                .userId(reservationEntity.getUserId())
                .phone(reservationEntity.getPhone())
                .partnerId(reservationEntity.getPartnerId())
                .storeName(reservationEntity.getStoreName())
                .people(reservationEntity.getPeople())
                .status(reservationEntity.getStatus())
                .time(reservationEntity.getTime())
                .build();
    }
}
