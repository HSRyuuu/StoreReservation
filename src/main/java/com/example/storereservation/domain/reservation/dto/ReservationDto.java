package com.example.storereservation.domain.reservation.dto;

import com.example.storereservation.domain.reservation.persist.ReservationEntity;
import com.example.storereservation.domain.reservation.type.ReservationStatus;
import lombok.Builder;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;
    private LocalDateTime statusUpdatedAt;


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
                .statusUpdatedAt(reservationEntity.getStatusUpdatedAt())
                .time(reservationEntity.getTime())
                .build();
    }
}
