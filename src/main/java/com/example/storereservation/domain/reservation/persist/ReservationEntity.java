package com.example.storereservation.domain.reservation.persist;

import com.example.storereservation.domain.reservation.type.ReservationStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

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

    @Setter
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;
    private LocalDateTime statusUpdatedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime time;
}
