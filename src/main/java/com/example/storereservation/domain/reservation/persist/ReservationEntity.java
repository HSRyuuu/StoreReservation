package com.example.storereservation.domain.reservation.persist;

import com.example.storereservation.domain.reservation.type.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "RESERVATION")
public class ReservationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
}
